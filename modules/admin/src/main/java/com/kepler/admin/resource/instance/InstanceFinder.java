package com.kepler.admin.resource.instance;

import java.util.Collection;

import com.kepler.admin.domain.ServiceAndVersion;

/**
 * 服务实例上下文(不含客户端)
 * 
 * @author kim 2015年12月16日
 */
public interface InstanceFinder {

	/**
	 * Tag集合(不含客户端)
	 * 
	 * @return
	 */
	public Collection<String> tags();

	/**
	 * 业务分组集合
	 * 
	 * @return
	 */
	public Collection<String> groups();

	/**
	 * 业务分组对应的业务名称集合
	 * 
	 * @param group
	 * @return
	 */
	public Collection<String> application(String group);

	/**
	 * 获取SID对应的服务实例集合(单主机可能会发布多个服务)
	 * 
	 * @param sid
	 * @return
	 */
	public Collection<Instance> sid(String sid);

	/**
	 * 获取Tag对应的服务实例集合
	 * 
	 * @param tag
	 * @return
	 */
	public Collection<Instance> tag(String tag);

	/**
	 * 获取业务分组对应的服务集合
	 * 
	 * @param group
	 * @return
	 */
	public Collection<Instance> group(String group);

	/**
	 * 获取业务分组和应用名称的对应的服务集合
	 * 
	 * @param group
	 * @param application
	 * @return
	 */
	public Collection<Instance> application(String group, String application);

	/**
	 * 获取指定服务 + 版本服务实例集合
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @return
	 */
	public Collection<Instance> service4version(String service, String versionAndCatalog);

	/**
	 * ZK路径对应的服务实例
	 * 
	 * @param path
	 * @return
	 */
	public Instance path(String path);

	/**
	 * 获取指定SID + Service + Version的服务实例(单主机可能会发布多个服务)
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @return
	 */
	public Instance sid(String sid, String service, String versionAndCatalog);

	/**
	 * 服务 + 版本集合
	 * 
	 * @return
	 */
	public Collection<ServiceAndVersion> service4versions();
}
