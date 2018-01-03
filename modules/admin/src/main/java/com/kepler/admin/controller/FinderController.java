package com.kepler.admin.controller;

import java.util.Collection;
import java.util.TreeSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.admin.domain.ServiceAndVersion;
import com.kepler.admin.resource.instance.Instance;
import com.kepler.admin.resource.instance.InstanceFinder;
import com.kepler.admin.resource.instance.impl.InstanceServices;

/**
 * @author kim 2015年12月16日
 */
@Controller
@RequestMapping(value = "/finder")
public class FinderController {

	private final InstanceFinder finder;

	public FinderController(InstanceFinder finder) {
		super();
		this.finder = finder;
	}

	/**
	 * 排序
	 * 
	 * @param values
	 * @return
	 */
	private Collection<String> sort(Collection<String> values) {
		TreeSet<String> sort = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		sort.addAll(values);
		return sort;
	}

	/**
	 * 获取服务标签集合
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> tag() {
		return this.sort(this.finder.tags());
	}

	/**
	 * 获取服务分组集合
	 * 
	 * @return
	 */
	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> group() {
		return this.sort(this.finder.groups());
	}

	/**
	 * 获取服务名称集合
	 * 
	 * @return
	 */
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> application(String group) {
		return this.finder.application(group);
	}

	/**
	 * 获取服务实例(SID + Service + Version)
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @return
	 */
	@RequestMapping(value = "/instance/sid", method = RequestMethod.GET)
	@ResponseBody
	public Instance instance4sid(String sid, String service, String versionAndCatalog) {
		return this.finder.sid(sid, service, versionAndCatalog);
	}

	/**
	 * 获取指定Tag的服务实例集合(Instance = Service + SID)
	 * 
	 * @param tag
	 * @return
	 */
	@RequestMapping(value = "/instances/tag", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Instance> instance4tag(String tag) {
		return this.finder.tag(tag);
	}

	/**
	 * 获取指定SID的服务实例集合(Instance = Service + SID)
	 * 
	 * @param sid
	 * @return
	 */
	@RequestMapping(value = "/instances/sid", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Instance> instance4sid(String sid) {
		return this.finder.sid(sid);
	}

	/**
	 * 获取指定业务分组的服务实例集合(Instance = Service + SID)
	 * 
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "/instances/group", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Instance> instance4group(String group) {
		return this.finder.group(group);
	}

	/**
	 * 获取指定服务名称和服务版本的服务实例集合(Instance = Service + SID)
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @return
	 */
	@RequestMapping(value = "/instances/service4version", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Instance> instance4serviceAndVersion(String service, String versionAndCatalog) {
		return this.finder.service4version(service, versionAndCatalog);
	}

	/**
	 * 获取指定Tag的服务集合
	 * 
	 * @param tag
	 * @return
	 */
	@RequestMapping(value = "/service/tag", method = RequestMethod.GET)
	@ResponseBody
	public Collection<ServiceAndVersion> service4tag(String tag) {
		return new InstanceServices(this.finder.tag(tag));
	}

	/**
	 * 获取指定业务分组的服务集合
	 * 
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "/service/group", method = RequestMethod.GET)
	@ResponseBody
	public Collection<ServiceAndVersion> service4group(String group) {
		return new InstanceServices(this.finder.group(group));
	}

	/**
	 * 获取指定业务分组和业务名称的的服务集合
	 * 
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "/service/application", method = RequestMethod.GET)
	@ResponseBody
	public Collection<ServiceAndVersion> service4application(String group, String application) {
		return new InstanceServices(this.finder.application(group, application));
	}

	/**
	 * 获取指定服务名称及版本的服务集合
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @return
	 */
	@RequestMapping(value = "/service/service4version", method = RequestMethod.GET)
	@ResponseBody
	public Collection<ServiceAndVersion> service4serviceAndVersion(String service, String versionAndCatalog) {
		return new InstanceServices(this.finder.service4version(service, versionAndCatalog));
	}

}
