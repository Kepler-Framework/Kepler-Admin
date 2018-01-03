package com.kepler.admin.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 周期
 * 
 * @author kim 2015年12月21日
 */
public enum Period {

	DAY, HOUR, MINUTE, SECOND, MILLISECOND;

	private static final Map<Period, TimeUnit> UNITS = new HashMap<Period, TimeUnit>();

	private static final Map<Period, Integer> BASES = new HashMap<Period, Integer>();

	static {
		Period.UNITS.put(Period.DAY, TimeUnit.DAYS);
		Period.UNITS.put(Period.HOUR, TimeUnit.HOURS);
		Period.UNITS.put(Period.MINUTE, TimeUnit.MINUTES);
		Period.UNITS.put(Period.SECOND, TimeUnit.SECONDS);
		Period.UNITS.put(Period.MILLISECOND, TimeUnit.MILLISECONDS);
		Period.BASES.put(Period.DAY, 24 * 60 * 60 * 1000);
		Period.BASES.put(Period.HOUR, 60 * 60 * 1000);
		Period.BASES.put(Period.MINUTE, 60 * 1000);
		Period.BASES.put(Period.SECOND, 1000);
		Period.BASES.put(Period.MILLISECOND, 1);
	}

	/**
	 * 指定单位的周期转换为当前单位的周期(计算时区偏移量)
	 * 
	 * @param period 指定周期单位
	 * @param value  指定周期值
	 * @return
	 */
	public long period(Period period, long value) {
		return Period.UNITS.get(this).convert(period.convert(value), TimeUnit.MILLISECONDS);
	}

	/**
	 * 指定时间转换为当前单位的周期(计算时区偏移量)
	 * 
	 * @param date
	 * @return
	 */
	public long period(Date date) {
		return this.period(date.getTime());
	}

	/**
	 * 指定毫秒数转换为当前单位的周期(计算时区偏移量)
	 * 
	 * @param millis
	 * @return
	 */
	public long period(long millis) {
		return Period.UNITS.get(this).convert(millis + TimeZone.getDefault().getOffset(millis), TimeUnit.MILLISECONDS);
	}

	/**
	 * 当前毫秒数转换为当前周期(同时计算时区偏移量)
	 * 
	 * @return
	 */
	public long period() {
		return this.period(System.currentTimeMillis());
	}

	/**
	 * 周期转换为毫秒数(忽略时区偏移量)
	 * 
	 * @param period
	 * @return
	 */
	public long convert(long period) {
		return period * Period.BASES.get(this);
	}

	/**
	 * 周期转换为毫秒数(计算时区偏移量)
	 *
	 * @param period
	 * @param zone
	 * @return
	 */
	public long convert(long period, TimeZone zone) {
		long timestamp = period * Period.BASES.get(this);
		return timestamp - zone.getOffset(timestamp);
	}
}
