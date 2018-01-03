package com.kepler.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.admin.domain.Period;
import com.kepler.admin.traces.Trace;
import com.kepler.admin.traces.TraceCollector;
import com.kepler.admin.traces.TraceService;

/**
 * @author zhangjiehao 2016年3月17日
 */
@Controller
@RequestMapping(value = "/traces")
public class TraceController {

	private final TraceCollector collector;

	private final TraceService trace;

	public TraceController(TraceCollector collector, TraceService trace) {
		super();
		this.collector = collector;
		this.trace = trace;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public List<?> methods(String trace) {
		return this.trace.trace(trace);
	}

	@RequestMapping(value = "/causes", method = RequestMethod.GET)
	@ResponseBody
	public List<Trace> causes(String service, String versionAndCatalog, String method, Period period, int offset, int length) {
		return this.collector.causes(service, versionAndCatalog, method, period, offset, length);
	}
}