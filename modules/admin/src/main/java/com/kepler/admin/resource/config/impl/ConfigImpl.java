package com.kepler.admin.resource.config.impl;

import java.util.Map;
import com.kepler.admin.resource.OrderedMap;
import com.kepler.admin.resource.config.Config;

/**
 * @author kim 2015年12月26日
 */
public class ConfigImpl implements Config {

	private final Map<String, Object> config;

	private final String path;

	private final String sid;

	public ConfigImpl(String path, Map<String, Object> config) {
		super();
		this.config = new OrderedMap(config);
		// 拆分SID
		this.sid = (this.path = path).substring(this.path.lastIndexOf("/") + 1);
	}

	public String getSid() {
		return this.sid;
	}

	public String getPath() {
		return this.path;
	}

	public Map<String, Object> getConfig() {
		return this.config;
	}
}
