package com.kepler.admin.resource.instance;

import com.kepler.admin.domain.ServiceAndVersion;
import com.kepler.service.ServiceInstance;

/**
 * 服务实例
 * 
 * @author kim 2015年12月16日
 */
public interface Instance {

	public String getSid();

	public String getTag();

	/**
	 * ZK Path
	 * 
	 * @return
	 */
	public String getPath();

	public String getHost();

	/**
	 * 业务分组
	 * 
	 * @return
	 */
	public String getGroup();
	
	/**
	 * 业务应用
	 * 
	 * @return
	 */
	public String getApplication();

	public int getPriority();

	/**
	 * 服务节点
	 * 
	 * @return
	 */
	public ServiceInstance instance();
	
	/**
	 * 承载服务
	 * 
	 * @return
	 */
	public ServiceAndVersion getService();
}
