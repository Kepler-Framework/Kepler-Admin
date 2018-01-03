package com.kepler.admin.resource.instance.impl;

import com.kepler.admin.domain.ServiceAndVersion;
import com.kepler.admin.resource.instance.Instance;
import com.kepler.host.Host;
import com.kepler.org.apache.commons.lang.StringUtils;
import com.kepler.org.apache.commons.lang.builder.ToStringBuilder;
import com.kepler.service.ServiceInstance;

/**
 * @author kim 2015年12月16日
 */
public class InstanceImpl implements Instance {

	private final String path;

	private final ServiceInstance instance;

	private final ServiceAndVersion service;

	public InstanceImpl(String path, ServiceInstance instance) {
		super();
		this.path = path;
		this.instance = instance;
		this.service = new ServiceAndVersion(instance.service(), instance.versionAndCatalog());
	}

	public String getSid() {
		return this.instance.host().sid();
	}

	public String getTag() {
		return StringUtils.defaultString(this.instance.host().tag(), Host.TAG_DEF);
	}

	public String getHost() {
		return this.instance.host().address();
	}

	@Override
	public String getPath() {
		return this.path;
	}

	public String getGroup() {
		return StringUtils.defaultString(this.instance.host().group(), Host.GROUP_DEF);
	}

	public String getApplication() {
		return StringUtils.defaultString(this.instance.host().name(), "unknow");
	}

	public int getPriority() {
		return this.instance.host().priority();
	}

	@Override
	public ServiceInstance instance() {
		return this.instance;
	}

	public ServiceAndVersion getService() {
		return this.service;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
