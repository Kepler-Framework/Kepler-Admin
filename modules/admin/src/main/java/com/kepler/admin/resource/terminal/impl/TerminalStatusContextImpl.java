package com.kepler.admin.resource.terminal.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;

import com.kepler.admin.resource.terminal.TerminalStatus;
import com.kepler.admin.resource.terminal.TerminalStatusContext;
import com.kepler.admin.resource.terminal.TerminalStatusFinder;
import com.kepler.host.HostStatus;
import com.kepler.serial.Serials;
import com.kepler.zookeeper.ZkContext;

/**
 * @author kim 2015年12月26日
 */
public class TerminalStatusContextImpl implements TerminalStatusFinder, TerminalStatusContext {

	private static final Log LOGGER = LogFactory.getLog(TerminalStatusContextImpl.class);

	// 业务分组对应的终端状态集合
	private final Map<String, List<String>> group = new HashMap<String, List<String>>();

	// 主机名称对应的终端状态集合
	private final Map<String, List<String>> host = new HashMap<String, List<String>>();

	// SID对应的终端状态
	private final Map<String, Dimension> sid = new HashMap<String, Dimension>();

	// ZK路径对应的终端状态
	private final Map<String, String> path = new HashMap<String, String>();

	private final Object lock = new Object();

	private final CuratorFramework client;

	private final Serials serials;

	public TerminalStatusContextImpl(CuratorFramework client, Serials serials) {
		this.serials = serials;
		this.client = client;
	}

	/**
	 * 获取指定Path的TerminalStatus
	 * 
	 * @param path
	 * @return
	 */
	private TerminalStatus get(String path) {
		try {
			return new TerminalStatusImpl(path, this.serials.def4input().input(this.client.getData().forPath(path), HostStatus.class));
		} catch (Exception e) {
			TerminalStatusContextImpl.LOGGER.error(e.getMessage(), e);
			return new UnvalidTerminalStatus(path);
		}
	}

	/**
	 * 工具方法
	 * 
	 * @param condition
	 * @param key
	 * @param status
	 */
	private void upsert(Map<String, List<String>> condition, String key, String path) {
		List<String> statuses = condition.get(key);
		condition.put(key, (statuses = statuses != null ? statuses : new ArrayList<String>()));
		statuses.add(path);
	}

	/**
	 * 工具方法, 如果Map中指定List为空则进行删除(由调用方进行同步)
	 * 
	 * @param condition
	 * @param key
	 * @param status
	 */
	private void remove(Map<String, List<String>> condition, String key, String path) {
		List<String> statuses = condition.get(key);
		if (statuses != null) {
			statuses.remove(path);
			// 级联删除, 如果集合为空则删除对应集合
			if (statuses.isEmpty()) {
				condition.remove(key);
			}
		}
	}

	public Collection<TerminalStatus> host(String host) {
		List<TerminalStatus> hosts = new ArrayList<TerminalStatus>();
		List<String> paths = this.host.get(host);
		if (paths != null) {
			for (String path : paths) {
				hosts.add(this.get(path));
			}
		}
		return hosts;
	}

	public Collection<TerminalStatus> group(String group) {
		List<TerminalStatus> groups = new ArrayList<TerminalStatus>();
		List<String> paths = this.group.get(group);
		if (paths != null) {
			for (String path : paths) {
				groups.add(this.get(path));
			}
		}
		return groups;
	}

	public Collection<TerminalStatus> application(String group, String application) {
		List<TerminalStatus> matched = new ArrayList<TerminalStatus>();
		for (TerminalStatus terminal : this.group(group)) {
			if (terminal.getApplication().equals(application)) {
				// 仅加载符合应用名称的终端
				matched.add(terminal);
			}
		}
		return matched;
	}

	public Collection<String> applications(String group) {
		Set<String> applications = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (TerminalStatus terminal : this.group(group)) {
			applications.add(terminal.getApplication());
		}
		return applications;
	}

	public Collection<String> groups() {
		TreeSet<String> groups = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		groups.addAll(this.group.keySet());
		return groups;
	}

	@Override
	public TerminalStatus sid(String sid) {
		return this.get(ZkContext.ROOT + ZkContext.STATUS + "/" + sid);
	}

	@Override
	// 强一致
	public TerminalStatusContextImpl insert(TerminalStatus status) {
		synchronized (this.lock) {
			this.upsert(this.group, status.getGroup(), status.getPath());
			this.upsert(this.host, status.getHost(), status.getPath());
			this.sid.put(status.getSid(), new Dimension(status));
			this.path.put(status.getPath(), status.getSid());
			return this;
		}
	}

	@Override
	public TerminalStatusContextImpl update(TerminalStatus status) {
		synchronized (this.lock) {
			return this.remove(status.getPath()).insert(status);
		}
	}

	@Override
	public TerminalStatusContextImpl remove(String path) {
		synchronized (this.lock) {
			// 删除Path获取Sid, 删除Sid获取Dimension, 完全删除
			Dimension dimension = this.sid.remove(this.path.remove(path));
			this.remove(this.group, dimension.group(), path);
			this.remove(this.host, dimension.host(), path);
			return this;
		}
	}

	private class Dimension {

		private final String group;

		private final String host;

		private Dimension(TerminalStatus status) {
			super();
			this.group = status.getGroup();
			this.host = status.getHost();
		}

		public String group() {
			return this.group;
		}

		public String host() {
			return this.host;
		}
	}

	/**
	 * 无效的终端(未上传信息)
	 * 
	 * @author KimShen
	 *
	 */
	private class UnvalidTerminalStatus implements TerminalStatus {

		private static final long serialVersionUID = 1L;

		private final String path;

		private UnvalidTerminalStatus(String path) {
			super();
			this.path = path;
		}

		@Override
		public String getSid() {
			return "";
		}

		@Override
		public String getPid() {
			return "";
		}

		public String getPort() {
			return "";
		}

		@Override
		public String getHost() {
			return "Unvalid host";
		}

		@Override
		public String getGroup() {
			return "";
		}

		@Override
		public String getApplication() {
			return "";
		}

		@Override
		public Map<String, Object> getStatus() {
			return null;
		}

		@Override
		public String getPath() {
			return this.path;
		}
	}
}
