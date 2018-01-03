package com.kepler.admin.generic;

import com.kepler.service.Service;

/**
 * @author KimShen
 *
 */
public interface GenericTemplate {

	/**
	 * 获取模板
	 * 
	 * @param service
	 * @param method
	 * @return
	 */
	public GenericRequest get(Service service, String method);

	/**
	 * 保存模板
	 * 
	 * @param request
	 */
	public void set(GenericRequest request);
}
