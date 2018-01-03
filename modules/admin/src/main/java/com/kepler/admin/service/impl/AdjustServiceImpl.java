package com.kepler.admin.service.impl;

import java.util.Map;

import org.apache.curator.framework.CuratorFramework;

import com.kepler.admin.resource.config.Config;
import com.kepler.admin.resource.config.ConfigFinder;
import com.kepler.admin.resource.instance.Instance;
import com.kepler.admin.resource.instance.InstanceFinder;
import com.kepler.admin.service.AdjustService;
import com.kepler.host.impl.ServerHost.Builder;
import com.kepler.serial.Serials;
import com.kepler.service.ServiceInstance;
import com.kepler.zookeeper.ZkSerial;

/**
 * @author longyaokun 2015年12月17日
 */
public class AdjustServiceImpl implements AdjustService {

	private final Serials serials;

	private final CuratorFramework client;

	private final ConfigFinder finder4config;

	private final InstanceFinder finder4instance;

	public AdjustServiceImpl(Serials serials, CuratorFramework client, ConfigFinder finder4config, InstanceFinder finder4instance) {
		super();
		this.client = client;
		this.serials = serials;
		this.finder4config = finder4config;
		this.finder4instance = finder4instance;
	}

	public void priority(String service, String versionAndCatalog, int priority) throws Exception {
		// 获取指定服务 + 版本所有的服务实例并调整优先级
		for (Instance instance : this.finder4instance.service4version(service, versionAndCatalog)) {
			this.client.setData().withVersion(-1).forPath(instance.getPath(), this.serials.def4output().output(new ZkSerial(new Builder(instance.instance().host()).setPriority(priority).toServerHost(), instance.instance()), ServiceInstance.class));
		}
	}

	@Override
	public void priority(String sid, String service, String versionAndCatalog, int priority) throws Exception {
		Instance instance = this.finder4instance.sid(sid, service, versionAndCatalog);
		this.client.setData().withVersion(-1).forPath(instance.getPath(), this.serials.def4output().output(new ZkSerial(new Builder(instance.instance().host()).setPriority(priority).toServerHost(), instance.instance()), ServiceInstance.class));
	}

	public void tag(String service, String versionAndCatalog, String tag) throws Exception {
		// 获取指定服务 + 版本所有的服务实例并调整Tag
		for (Instance instance : this.finder4instance.service4version(service, versionAndCatalog)) {
			this.client.setData().withVersion(-1).forPath(instance.getPath(), this.serials.def4output().output(new ZkSerial(new Builder(instance.instance().host()).setTag(tag).toServerHost(), instance.instance()), ServiceInstance.class));
		}
	}

	@Override
	public void tag(String sid, String service, String versionAndCatalog, String tag) throws Exception {
		Instance instance = this.finder4instance.sid(sid, service, versionAndCatalog);
		this.client.setData().withVersion(-1).forPath(instance.getPath(), this.serials.def4output().output(new ZkSerial(new Builder(instance.instance().host()).setTag(tag).toServerHost(), instance.instance()), ServiceInstance.class));
	}

	@Override
	public void config(String sid, Map<String, String> config) throws Exception {
		Config current = this.finder4config.sid(sid);
		this.client.setData().withVersion(-1).forPath(current.getPath(), this.serials.def4output().output(config, Map.class));
	}
}
