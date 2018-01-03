package com.kepler.admin.resource.instance.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.kepler.admin.domain.ServiceAndVersion;
import com.kepler.admin.resource.instance.Instance;
import com.kepler.admin.resource.instance.InstanceContext;
import com.kepler.admin.resource.instance.InstanceFinder;
import com.kepler.org.apache.commons.lang.builder.EqualsBuilder;
import com.kepler.org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author kim 2015年12月16日
 */
public class InstanceContextImpl implements InstanceFinder, InstanceContext {

	/**
	 * Service + Version所对应的服务实例集合
	 */
	private final Map<ServiceAndVersion, List<Instance>> service4version = new HashMap<ServiceAndVersion, List<Instance>>();

	/**
	 * SID所对应的服务实例集合
	 */
	private final Map<String, List<Instance>> sid4instances = new HashMap<String, List<Instance>>();

	/**
	 * 业务分组所对应的服务实例集合
	 */
	private final Map<String, List<Instance>> group = new HashMap<String, List<Instance>>();

	/**
	 * Tag所对应的服务实例集合
	 */
	private final Map<String, List<Instance>> tag = new HashMap<String, List<Instance>>();

	/**
	 * SID = (SID + Service/Version维度)对应服务实例, 获取指定终端上的指定服务 
	 */
	private final Map<SID, Instance> sid4instance = new HashMap<SID, Instance>();

	/**
	 * ZK路径对应的服务实例
	 */
	private final Map<String, Instance> path = new HashMap<String, Instance>();

	private final Object lock = new Object();

	public Collection<ServiceAndVersion> service4versions() {
		return this.service4version.keySet();
	}

	public Collection<String> application(String group) {
		Set<String> applications = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (Instance instance : this.group(group)) {
			// 迭代所有符合的实例并加载名称
			applications.add(instance.getApplication());
		}
		return applications;
	}

	public Collection<String> groups() {
		return this.group.keySet();
	}

	public Collection<String> tags() {
		return this.tag.keySet();
	}

	public Collection<Instance> service4version(String service, String versionAndCatalog) {
		return this.service4version.get(new ServiceAndVersion(service, versionAndCatalog));
	}

	public Collection<Instance> application(String group, String application) {
		Collection<Instance> matched = new ArrayList<Instance>();
		for (Instance instance : this.group.get(group)) {
			// 迭代所有符合的实例并加载
			if (instance.getApplication().equals(application)) {
				matched.add(instance);
			}
		}
		return matched;
	}

	public Collection<Instance> group(String group) {
		return this.group.get(group);
	}

	public Collection<Instance> sid(String sid) {
		return this.sid4instances.get(sid);
	}

	public Collection<Instance> tag(String tag) {
		return this.tag.get(tag);
	}

	public Instance path(String path) {
		return this.path.get(path);
	}

	public Instance sid(String sid, String service, String versionAndCatalog) {
		return this.sid4instance.get(new SID(sid, service, versionAndCatalog));
	}

	/**
	 * 工具方法
	 * 
	 * @param condition
	 * @param key
	 * @param instance
	 */
	private <T> void upsert(Map<T, List<Instance>> condition, T key, Instance instance) {
		List<Instance> instances = condition.get(key);
		condition.put(key, (instances = instances != null ? instances : new ArrayList<Instance>()));
		instances.add(instance);
	}

	/**
	 * 工具方法, 如果Map中指定List为空则进行删除(由调用方进行同步)
	 * 
	 * @param condition
	 * @param key
	 * @param instance
	 */
	private <T> void remove(Map<T, List<Instance>> condition, T key, Instance instance) {
		List<Instance> instances = condition.get(key);
		instances.remove(instance);
		if (instances.isEmpty()) {
			condition.remove(key);
		}
	}

	/**
	 * 从所有维度中删除服务实例
	 * 
	 * @param instance
	 * @return
	 */
	private InstanceContextImpl remove(Instance instance) {
		this.remove(this.service4version, instance.getService(), instance);
		this.remove(this.sid4instances, instance.getSid(), instance);
		this.remove(this.group, instance.getGroup(), instance);
		this.remove(this.tag, instance.getTag(), instance);
		this.sid4instance.remove(new SID(instance));
		this.path.remove(instance.getPath());
		return this;
	}

	@Override
	// 维度Key不同, 强一致
	public InstanceContextImpl insert(Instance instance) {
		synchronized (this.lock) {
			this.upsert(this.service4version, instance.getService(), instance);
			this.upsert(this.sid4instances, instance.getSid(), instance);
			this.upsert(this.group, instance.getGroup(), instance);
			this.upsert(this.tag, instance.getTag(), instance);
			this.sid4instance.put(new SID(instance), instance);
			this.path.put(instance.getPath(), instance);

		}
		return this;
	}

	public InstanceContextImpl update(Instance instance) {
		synchronized (this.lock) {
			return this.remove(this.path.get(instance.getPath())).insert(instance);
		}
	}

	@Override
	public InstanceContextImpl remove(String path) {
		synchronized (this.lock) {
			return this.remove(this.path.get(path));
		}
	}

	private class SID {

		private final String sid;

		private final String service;

		private final String versionAndCatalog;

		private SID(Instance instance) {
			this(instance.instance().host().sid(), instance.getService().getService(), instance.getService().getVersionAndCatalog());
		}

		private SID(String sid, String service, String versionAndCatalog) {
			super();
			this.sid = sid;
			this.service = service;
			this.versionAndCatalog = versionAndCatalog;
		}

		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.sid).append(this.service).append(this.versionAndCatalog).toHashCode();
		}

		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
	}
}
