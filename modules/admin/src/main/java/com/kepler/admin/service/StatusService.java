package com.kepler.admin.service;

import java.util.Map;

/**
 * @author kim 2016年1月2日
 */
public interface StatusService {

	/**
	 * 终端状态
	 * 
	 * @param sid
	 * @return
	 */
	public Map<String, String> status(String sid);
}
