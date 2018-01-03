package com.kepler.admin.controller;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.admin.resource.terminal.TerminalStatus;
import com.kepler.admin.resource.terminal.TerminalStatusFinder;

/**
 * 实例查询
 * 
 * @author KimShen
 *
 */
@Controller
@RequestMapping(value = "/terminal")
public class TerminalController {

	private final TerminalStatusFinder finder;

	public TerminalController(TerminalStatusFinder finder) {
		super();
		this.finder = finder;
	}

	/**
	 * 所有终端的业务分组集合(包含客户端)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> groups() {
		return this.finder.groups();
	}

	/**
	 * 所有终端的业务分组集合(包含客户端)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> applications(String group) {
		return this.finder.applications(group);
	}

	/**
	 * 业务分组对应主机状态集合
	 * 
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "/group", method = RequestMethod.GET)
	@ResponseBody
	public Collection<TerminalStatus> group(String group) {
		return this.finder.group(group);
	}

	/**
	 * 主机名称对应主机状态集合
	 * 
	 * @param host
	 * @return
	 */
	@RequestMapping(value = "/host", method = RequestMethod.GET)
	@ResponseBody
	public Collection<TerminalStatus> host(String host) {
		return this.finder.host(host);
	}

	/**
	 * 业务分组和业务名称对应主机状态集合
	 * 
	 * @param group
	 * @return
	 */
	@RequestMapping(value = "/application", method = RequestMethod.GET)
	@ResponseBody
	public Collection<TerminalStatus> application(String group, String application) {
		return this.finder.application(group, application);
	}

	/**
	 * 主机状态
	 * 
	 * @param sid
	 * @return
	 */
	@RequestMapping(value = "/sid", method = RequestMethod.GET)
	@ResponseBody
	public TerminalStatus sid(String sid) {
		return this.finder.sid(sid);
	}
}
