package com.kepler.admin.statistics.method;

import java.util.Collection;
import java.util.List;

import com.kepler.admin.domain.Period;

/**
 * @author kim 2015年12月21日
 */
public interface MethodInvokerService {

	/**
	 * SID + Service + Version维度方法访问质量
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @return
	 */
	public Collection<MethodInvoker> methods(String sid, String service, String versionAndCatalog, Period period, int offset, int length);

	/**
	 * Service + Version维度方法访问质量
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param period 周期单位
	 * @param offset 偏移
 	 * @param length 长度
	 * @return
	 */
	public Collection<MethodInvoker> methods4service(String service, String versionAndCatalog, Period period, int offset, int length);

	/**
	 * group 维度的方法访问质量
	 * 
	 * @param group
	 * @param application
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @param sort 	 排序器
	 * @return
	 */
	public List<MethodInvoker> methods4group(String group, String application, Period period, int offset, int length, SortBy sort);
}
