package com.kepler.admin.resource.terminal.impl;

import java.util.Map;

import com.kepler.admin.resource.OrderedMap;
import com.kepler.admin.resource.terminal.TerminalStatus;
import com.kepler.host.Host;
import com.kepler.host.HostStatus;
import com.kepler.org.apache.commons.lang.StringUtils;

/**
 * @author kim 2015年12月26日
 */
public class TerminalStatusImpl implements TerminalStatus {

	private static final long serialVersionUID = 1L;

	private final Map<String, Object> data;

	private final HostStatus status;

	private final String path;

	public TerminalStatusImpl(String path, HostStatus status) {
		this.path = path;
		this.status = status;
		this.data = new OrderedMap(status.getStatus());
	}

	@Override
	public String getSid() {
		return this.status.getSid();
	}

	@Override
	public String getPid() {
		return this.status.getPid();
	}

	@Override
	public String getPath() {
		return this.path;
	}

	public String getPort() {
		return this.status.getPort();
	}

	@Override
	public String getHost() {
		return this.status.getHost();
	}

	@Override
	public String getGroup() {
		return StringUtils.defaultString(this.status.getGroup(), Host.GROUP_DEF);
	}

	@Override
	public String getApplication() {
		return StringUtils.defaultString(this.status.getApplication(), "unknow");
	}

	@Override
	public Map<String, Object> getStatus() {
		return this.data;
	}
}
