package com.kepler.admin.statistics.method;

/**
 * @author longyaokun 2015年12月21日
 */
public class MethodInvoker {

	private final String service;

	private final String version;

	private final String name;

	private final long max;

	private final long total;

	private final long timeout;

	private final long exception;

	private final double rtt;

	public MethodInvoker(String service, String version, String name, long total, long timeout, long exception, long max, double rtt) {
		super();
		this.service = service;
		this.version = version;
		this.rtt = rtt;
		this.max = max;
		this.name = name;
		this.total = total;
		this.timeout = timeout;
		this.exception = exception;
	}

	public String getName() {
		return this.name;
	}

	public double getRtt() {
		return this.rtt;
	}

	public long getMax() {
		return this.max;
	}

	public long getTotal() {
		return this.total;
	}

	public long getTimeout() {
		return this.timeout;
	}

	public long getException() {
		return this.exception;
	}

	public String getService() {
		return this.service;
	}

	public String getVersion() {
		return this.version;
	}
}
