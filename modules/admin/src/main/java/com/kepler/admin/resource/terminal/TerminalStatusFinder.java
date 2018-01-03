package com.kepler.admin.resource.terminal;

import java.util.Collection;

/**
 * @author kim 2016年1月4日
 */
public interface TerminalStatusFinder {
	
	/**
	 * 主机名称对应终端状态集合
	 * 
	 * @param group
	 * @return
	 */
	public Collection<TerminalStatus> host(String host);

	/**
	 * 业务分组对应终端状态集合
	 * 
	 * @param group
	 * @return
	 */
	public Collection<TerminalStatus> group(String group);

	/**
	 * 业务分组和业务名称对应终端状态集合
	 * 
	 * @param group
	 * @return
	 */
	public Collection<TerminalStatus> application(String group, String application);

	/**
	 * 业务名称集合(含客户端)
	 * 
	 * @param group
	 * @return
	 */
	public Collection<String> applications(String group);

	/**
	 * 业务分组集合(含客户端)
	 * 
	 * @param group
	 * @return
	 */
	public Collection<String> groups();

	/**
	 * SID对应终端状态
	 * 
	 * @param sid
	 * @return
	 */
	public TerminalStatus sid(String sid);
}
