package com.kepler.admin.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kepler.KeplerGenericException;
import com.kepler.admin.generic.GenericRequest;
import com.kepler.admin.generic.GenericTemplate;
import com.kepler.admin.generic.impl.DefaultRequest;
import com.kepler.generic.reflect.GenericService;
import com.kepler.header.HeadersContext;
import com.kepler.header.impl.TraceContext;
import com.kepler.host.Host;
import com.kepler.org.apache.commons.lang.builder.ToStringBuilder;
import com.kepler.service.Service;

/**
 * @author KimShen
 *
 */
@Controller
@RequestMapping(value = "/generic")
public class GenericController {

	private static final Log LOGGER = LogFactory.getLog(GenericController.class);

	private final GenericTemplate template;

	private final GenericService generic;

	private final HeadersContext headers;

	public GenericController(GenericTemplate template, GenericService generic, HeadersContext headers) {
		super();
		this.generic = generic;
		this.headers = headers;
		this.template = template;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public DefaultResponse generic(@RequestBody DefaultRequest request) {
		// 指定Tag
		this.headers.get().put(Host.TAG_KEY, request.getTag());
		DefaultResponse response = new DefaultResponse(TraceContext.getTrace());
		try {
			GenericController.LOGGER.info("[request=" + ToStringBuilder.reflectionToString(request) + "]");
			// 发送请求
			response.response(this.generic.invoke(request.metadata(), request.getMethod(), request.getClasses(), request.getDatas()));
			// 记录历史(仅服务正常返回时)
			GenericController.LOGGER.info("[response=" + ToStringBuilder.reflectionToString(response) + "]");
			this.template.set(request);
			return response;
		} catch (Throwable e) {
			return response.response(e);
		} finally {
			// 清除Tag
			this.headers.get().delete(Host.TAG_KEY);
			// 清除Trace
			TraceContext.release();
		}
	}

	/**
	 * 获取指定服务的历史调用信息
	 * 
	 * @param service
	 * @param version
	 * @param catalog
	 * @param method
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public GenericRequest template(String service, String version, String catalog, String method) {
		return this.template.get(new Service(service, version, catalog), method);
	}

	/**
	 * 用于响应
	 * 
	 * @author KimShen
	 *
	 */
	public static class DefaultResponse {

		private final long start = System.currentTimeMillis();

		private final String trace;

		private ThrowableCause throwable;

		private Object response;

		private long elapse;

		public DefaultResponse(String trace) {
			super();
			this.trace = trace;
		}

		/**
		 * 指定异常
		 * 
		 * @param throwable
		 */
		public DefaultResponse response(Throwable throwable) {
			this.throwable = new ThrowableCause(throwable);
			this.elapse = System.currentTimeMillis() - this.start;
			return this;
		}

		/**
		 * 指定结果
		 * 
		 * @param response
		 */
		public DefaultResponse response(Object response) {
			this.response = response;
			this.elapse = System.currentTimeMillis() - this.start;
			return this;
		}

		public ThrowableCause getThrowable() {
			return this.throwable;
		}

		public Object getResponse() {
			return this.response;
		}

		public String getTrace() {
			return this.trace;
		}

		public long getElapse() {
			return this.elapse;
		}
	}

	/**
	 * 用于异常分析
	 * 
	 * @author KimShen
	 *
	 */
	public static class ThrowableCause {

		private final Map<String, Object> params;

		private final String message;

		public ThrowableCause(Throwable throwable) {
			super();
			// 泛化异常分析
			if (KeplerGenericException.class.isAssignableFrom(throwable.getClass())) {
				KeplerGenericException generic = KeplerGenericException.class.cast(throwable);
				this.message = generic.getMessage();
				this.params = generic.getFields();
			} else {
				this.message = throwable.getMessage();
				this.params = null;
			}
		}

		public Map<String, Object> getParams() {
			return this.params;
		}

		public String getMessage() {
			return this.message;
		}
	}
}
