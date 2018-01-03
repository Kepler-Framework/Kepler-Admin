package com.kepler.admin.statistics.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author longyaokun
 * 
 */
public class ChartLine {

	/**
	 * 访问量
	 */
	public static final String CHART_TYPE_TOTAL = "total";

	/**
	 * 错误率
	 */
	public static final String CHART_TYPE_ERROR = "error";

	/**
	 * 耗时
	 */
	public static final String CHART_TYPE_ELAPSE = "elapse";

	public static final String CHART_TYPE_DEFAULT = "undefined";

	private final List<Object[]> data = new ArrayList<Object[]>();

	private final String title;

	private final String type;

	/**
	 * @param type 类型
	 * @param title 标题
	 */
	public ChartLine(String type, String title) {
		super();
		this.type = type;
		this.title = title;
	}

	public String getType() {
		return this.type;
	}

	public String getTitle() {
		return this.title;
	}

	public ChartLine add(Object[] data) {
		this.data.add(data);
		return this;
	}

	public List<Object[]> getData() {
		return this.data;
	}

	public static ChartLine def(String title) {
		return new ChartLine(ChartLine.CHART_TYPE_DEFAULT, title);
	}

	public static ChartLine elapse(String title) {
		return new ChartLine(ChartLine.CHART_TYPE_ELAPSE, title);
	}

	public static ChartLine total(String title) {
		return new ChartLine(ChartLine.CHART_TYPE_TOTAL, title);
	}

	public static ChartLine error(String title) {
		return new ChartLine(ChartLine.CHART_TYPE_ERROR, title);
	}
}
