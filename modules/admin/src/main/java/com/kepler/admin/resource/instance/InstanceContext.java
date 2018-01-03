package com.kepler.admin.resource.instance;

/**
 * @author kim 2015年12月16日
 */
public interface InstanceContext {

	public InstanceContext insert(Instance instance);

	public InstanceContext update(Instance instance);

	public InstanceContext remove(String path);
}
