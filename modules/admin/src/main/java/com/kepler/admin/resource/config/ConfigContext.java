package com.kepler.admin.resource.config;

/**
 * 动态参数容器
 * 
 * @author kim 2016年1月2日
 */
public interface ConfigContext {

	public ConfigContext update(String config);

	public ConfigContext insert(String config);

	public ConfigContext remove(String path);
}
