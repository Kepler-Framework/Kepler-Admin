package com.kepler.admin.statistics.barchart;

import java.util.ArrayList;
import java.util.List;

/**
 * 水桶图数据结构
 * 
 * @author longyaokun 2016年3月21日
 *
 */
public class BarChartData {

	private final List<String> categories = new ArrayList<String>();

	private final List<BarChartSeries> barChartSeries = new ArrayList<BarChartSeries>();

	public List<String> getCategories() {
		return this.categories;
	}

	public List<BarChartSeries> getSeries() {
		return this.barChartSeries;
	}

	public void addCategory(String category) {
		this.categories.add(category);
	}

	public void addSeries(BarChartSeries barChartSeries) {
		this.barChartSeries.add(barChartSeries);
	}
}
