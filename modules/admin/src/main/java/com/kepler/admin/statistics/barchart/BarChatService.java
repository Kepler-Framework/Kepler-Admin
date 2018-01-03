package com.kepler.admin.statistics.barchart;

import java.util.List;

import com.kepler.admin.domain.Period;

/**
 * 水桶图服务
 * 
 * @author longyaokun 2016年3月21日
 *
 */
public interface BarChatService {

	/**
	 * @param service
	 * @param versionAndCatalog
	 * @param method
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @return
	 */
	public List<BarChartData> statistics(String service, String versionAndCatalog, String method, Period period, int offset, int length);
}
