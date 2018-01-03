package com.kepler.admin.service;

import java.util.Map;

/**
 * @author longyaokun 2015年12月17日
 */
public interface AdjustService {

	/**
	 * 修改优先级(Service + Version)
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param priority
	 * @throws Exception
	 */
	public void priority(String service, String versionAndCatalog, int priority) throws Exception;

	/**
	 * 修改优先级(SID + Service + Version)
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param priority
	 * @throws Exception
	 */
	public void priority(String sid, String service, String versionAndCatalog, int priority) throws Exception;

	/**
	 * 修改Tag(Service + Version)
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param tag
	 * @throws Exception
	 */
	public void tag(String service, String versionAndCatalog, String tag) throws Exception;

	/**
	 * 修改Tag(SID + Service + Version)
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param tag
	 * @throws Exception
	 */
	public void tag(String sid, String service, String versionAndCatalog, String tag) throws Exception;

	/**
	 * 动态参数
	 * 
	 * @param sid
	 * @param config
	 * @throws Exception
	 */
	public void config(String sid, Map<String, String> config) throws Exception;
}
