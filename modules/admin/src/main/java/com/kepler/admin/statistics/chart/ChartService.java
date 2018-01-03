package com.kepler.admin.statistics.chart;

import com.kepler.admin.domain.Period;

/**
 * @author longyaokun 2015年12月21日
 */
public interface ChartService {

	/**
	 * Service + Version图表
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param period
	 * @param offset
	 * @param length
	 * @return
	 */
	public ChartDataset service(String service, String versionAndCatalog, Period period, int offset, int length);

	/**
	 * SID + Service + Version图表
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param period
	 * @param offset
 	 * @param length
	 * @return
	 */
	public ChartDataset instance(String sid, String service, String versionAndCatalog, Period period, int offset, int length);
}
