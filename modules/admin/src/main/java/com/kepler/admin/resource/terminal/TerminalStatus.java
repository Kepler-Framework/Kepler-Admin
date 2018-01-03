package com.kepler.admin.resource.terminal;

import com.kepler.host.HostStatus;

/**
 * 包含ZK路径的主机状态
 * 
 * @author kim 2016年1月4日
 */
public interface TerminalStatus extends HostStatus {

	public String getPath();
}
