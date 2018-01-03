package com.kepler.admin.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.admin.domain.Period;
import com.kepler.admin.statistics.chart.ChartDataset;
import com.kepler.admin.statistics.chart.ChartService;
import com.kepler.admin.statistics.chart.StatusDataset;
import com.kepler.admin.statistics.chart.StatusService;

/**
 * @author longyaokun 2015年12月16日
 * 
 */
@Controller
@RequestMapping(value = "/chart")
public class ChartController {

	private final ChartService chart;

	private final StatusService status;

	public ChartController(ChartService chart, StatusService status) {
		super();
		this.chart = chart;
		this.status = status;
	}

	/**
	 * 服务维度图表
	 * 
	 * @param service
	 * @param versionAndCatalog
	 * @param period 周期单位
	 * @param offset 偏移
	 * @return
	 */
	@RequestMapping(value = "/service", method = RequestMethod.GET)
	@ResponseBody
	public ChartDataset service(String service, String versionAndCatalog, Period period, int offset, int length) {
		return this.chart.service(service, versionAndCatalog, period, offset, length);
	}

	/**
	 * 服务实例维度图表
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @return
	 */
	@RequestMapping(value = "/instance", method = RequestMethod.GET)
	@ResponseBody
	public ChartDataset instance(String sid, String service, String versionAndCatalog, Period period, int offset, int length) {
		return this.chart.instance(sid, service, versionAndCatalog, period, offset, length);
	}

	/**
	 * 主机状态维度图表
	 * 
	 * @param sid
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @return
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	@ResponseBody
	public StatusDataset status(String sid, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
		return this.status.status(sid, start, end);
	}
}
