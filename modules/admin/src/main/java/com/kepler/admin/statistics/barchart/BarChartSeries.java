package com.kepler.admin.statistics.barchart;

/**
 * 水桶图数据结构
 * 
 * @author longyaokun 2016年3月21日
 *
 */
public class BarChartSeries {

	private final String name;
	
	private final Object[] data;

	public BarChartSeries(String name, Object[] data) {
		this.name = name;
		this.data = data;
	}

	public Object[] getData() {
		return this.data;
	}

	public String getName() {
		return this.name;
	}
}
