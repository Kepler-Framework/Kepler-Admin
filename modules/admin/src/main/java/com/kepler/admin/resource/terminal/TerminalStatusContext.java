package com.kepler.admin.resource.terminal;


/**
 * @author kim 2016年1月4日
 */
public interface TerminalStatusContext {

	public TerminalStatusContext update(TerminalStatus status);

	public TerminalStatusContext insert(TerminalStatus status);

	public TerminalStatusContext remove(String path);
}
