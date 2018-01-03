package com.kepler.admin.traces;

import java.util.List;

import com.kepler.admin.domain.Period;

/**
 * @author KimShen
 *
 */
public interface TraceCollector {

	/**
	 * 区域时间内的Trace
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param method 
	 * @param period
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<Trace> causes(String service, String versionAndCatalog, String method, Period period, int offset, int length);
}
