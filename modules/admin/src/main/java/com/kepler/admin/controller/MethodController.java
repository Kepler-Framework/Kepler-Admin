package com.kepler.admin.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.admin.domain.Period;
import com.kepler.admin.statistics.method.MethodInvoker;
import com.kepler.admin.statistics.method.MethodInvokerService;
import com.kepler.admin.statistics.method.SortBy;

/**
 * @author longyaokun 2015年12月17日
 */
@Controller
public class MethodController {

	private final MethodInvokerService methods;

	public MethodController(MethodInvokerService methods) {
		super();
		this.methods = methods;
	}

	/**
	 * SID + Service + Version对应服务实例每个方法在指定周期内的统计
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param period
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/methods/instance", method = RequestMethod.GET)
	@ResponseBody
	public Collection<MethodInvoker> methods(String sid, String service, String versionAndCatalog, Period period, int offset, int length) {
		return this.methods.methods(sid, service, versionAndCatalog, period, offset, length);
	}

	/**
	 * Service + Version对应服务集合每个方法在指定周期内的统计(聚合)
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param period
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/methods/service", method = RequestMethod.GET)
	@ResponseBody
	public Collection<MethodInvoker> methods4Service(String service, String versionAndCatalog, Period period, int offset, int length) {
		return this.methods.methods4service(service, versionAndCatalog, period, offset, length);
	}

	/**
	 * 业务分组中每个方法在指定周期内的统计(聚合)
	 * 
	 * @param group
	 * @param period
	 * @param offset
	 * @param sortBy
	 * @return
	 */
	@RequestMapping(value = "/methods/group", method = RequestMethod.GET)
	@ResponseBody
	public List<MethodInvoker> methods4Group(String group, String application, Period period, int offset, int length, SortBy sortBy) {
		return this.methods.methods4group(group, application, period, offset, length, sortBy);
	}
}