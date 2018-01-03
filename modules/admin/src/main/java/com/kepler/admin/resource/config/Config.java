package com.kepler.admin.resource.config;

import java.util.Map;

/**
 * SID对应主机动态参数
 * 
 * @author kim 2015年12月26日
 */
public interface Config {
	
	public String getSid();

	/**
	 * ZK Path
	 * 
	 * @return
	 */
	public String getPath();

	/**
	 * 动态参数
	 * 
	 * @return
	 */
	public Map<String, Object> getConfig();
}
