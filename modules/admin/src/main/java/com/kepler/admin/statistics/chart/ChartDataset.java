package com.kepler.admin.statistics.chart;

/**
 * @author longyaokun
 * 
 */
public class ChartDataset {

	private final ChartLine elapse;

	private final ChartLine total;

	private final ChartLine error;

	public ChartDataset(String title) {
		super();
		this.error = ChartLine.error(title);
		this.total = ChartLine.total(title);
		this.elapse = ChartLine.elapse(title);
	}

	public ChartDataset total(Object[] data) {
		this.total.add(data);
		return this;
	}

	public ChartDataset elapse(Object[] data) {
		this.elapse.add(data);
		return this;
	}

	public ChartDataset error(Object[] data) {
		this.error.add(data);
		return this;
	}

	public ChartLine getElapse() {
		return this.elapse;
	}

	public ChartLine getTotal() {
		return this.total;
	}

	public ChartLine getError() {
		return this.error;
	}
}
