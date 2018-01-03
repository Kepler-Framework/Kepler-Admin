package com.kepler.admin.traces;

import java.util.List;

/**
 * @author zhangjiehao
 *
 */
public interface TraceService {

	/**
	 * 指定链路的Trace
	 * 
	 * @param trace
	 * @return
	 */
	public List<?> trace(String trace);
}
