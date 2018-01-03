package com.kepler.admin.resource.config;

import java.util.Collection;

/**
 * @author kim 2015年12月26日
 */
public interface ConfigFinder {

	/**
	 * 获取Key集合
	 * 
	 * @return
	 */
	public Collection<String> keys();

	/**
	 * 通过SID获取动态参数
	 * 
	 * @param sid
	 * @return
	 */
	public Config sid(String sid);
}
