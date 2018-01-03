package com.kepler.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.admin.domain.Period;
import com.kepler.admin.statistics.barchart.BarChartData;
import com.kepler.admin.statistics.barchart.BarChatService;

/**
 * 水桶图
 * 
 * @author longyaokun 2016年3月21日
 *
 */
@Controller
@RequestMapping(value = "/barchart")
public class BarChartController {

	private final BarChatService barChat;

	public BarChartController(BarChatService barChat) {
		this.barChat = barChat;
	}

	/**
	 * @param service 
	 * @param versionAndCatalog
	 * @param method
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<BarChartData> statistics4clients(String service, String versionAndCatalog, String method, Period period, int offset, int length) {
		return this.barChat.statistics(service, versionAndCatalog, method, period, offset, length);
	}
}
