package com.kepler.admin.statistics.chart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务静态静态统计
 * 
 * @author longyaokun
 * 
 */
public class StatusDataset {

	private Map<String, ChartLine> gc = new HashMap<String, ChartLine>();

	private ChartLine memoryMax;

	private ChartLine memoryUsed;

	private ChartLine trafficInput;

	private ChartLine trafficOutput;

	private ChartLine threadJvm;

	private ChartLine threadFramework;

	private ChartLine qualityWaiting;

	private ChartLine qualityBreaking;

	private ChartLine qualityDemoting;

	public StatusDataset(String title) {
		super();
		// Memory
		this.memoryMax = ChartLine.def(title);
		this.memoryUsed = ChartLine.def(title);
		// 流量
		this.trafficInput = ChartLine.def(title);
		this.trafficOutput = ChartLine.def(title);
		// Thread
		this.threadJvm = ChartLine.def(title);
		this.threadFramework = ChartLine.def(title);
		// 质量
		this.qualityWaiting = ChartLine.def(title);
		this.qualityBreaking = ChartLine.def(title);
		this.qualityDemoting = ChartLine.def(title);
	}

	public StatusDataset gc(Object[] data, String title) {
		// 获取对应GC数据并追加
		ChartLine chart = this.gc.get(title);
		chart = chart != null ? chart : ChartLine.def(title);
		chart.add(data);
		this.gc.put(title, chart);
		return this;
	}

	public StatusDataset memoryMax(Object[] data) {
		this.memoryMax.add(data);
		return this;
	}

	public StatusDataset memoryUsed(Object[] data) {
		this.memoryUsed.add(data);
		return this;
	}

	public StatusDataset threadJvm(Object[] data) {
		this.threadJvm.add(data);
		return this;
	}

	public StatusDataset threadFramework(Object[] data) {
		this.threadFramework.add(data);
		return this;
	}

	public StatusDataset qualityWaiting(Object[] data) {
		this.qualityWaiting.add(data);
		return this;
	}

	public StatusDataset qualityDemoting(Object[] data) {
		this.qualityDemoting.add(data);
		return this;
	}

	public StatusDataset qualityBreaking(Object[] data) {
		this.qualityBreaking.add(data);
		return this;
	}

	public StatusDataset trafficInput(Object[] data) {
		this.trafficInput.add(data);
		return this;
	}

	public StatusDataset trafficOutput(Object[] data) {
		this.trafficOutput.add(data);
		return this;
	}

	public ChartLine getMemoryMax() {
		return this.memoryMax;
	}

	public ChartLine getMemoryUsed() {
		return this.memoryUsed;
	}

	public ChartLine getThreadJvm() {
		return this.threadJvm;
	}

	public ChartLine getThreadFramework() {
		return this.threadFramework;
	}

	public ChartLine getTrafficInput() {
		return this.trafficInput;
	}

	public ChartLine getTrafficOutput() {
		return this.trafficOutput;
	}

	public ChartLine getQualityWaiting() {
		return this.qualityWaiting;
	}

	public ChartLine getQualityDemoting() {
		return this.qualityDemoting;
	}

	public ChartLine getQualityBreaking() {
		return this.qualityBreaking;
	}

	public Collection<ChartLine> getGc() {
		return this.gc.values();
	}
}
