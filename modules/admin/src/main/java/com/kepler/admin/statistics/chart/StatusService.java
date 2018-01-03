package com.kepler.admin.statistics.chart;

import java.util.Date;

/**
 * @author longyaokun
 *
 */
public interface StatusService {

	/**
	 * SID指定的动态状态聚合
	 * 
	 * @param sid
	 * @param start 起始偏移量
	 * @param end 	终止偏移量
	 * @return
	 */
	public StatusDataset status(String sid, Date start, Date end);
}
