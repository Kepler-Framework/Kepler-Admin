package com.kepler.admin.mongo;

/**
 * Mongo 字典
 * 
 * @author kim 2013-11-15
 */
public interface Dictionary {
	/**
	 * 数据库名称
	 */
	public static final String D_NAME = "D_NAME";
	/**
	 * 数据集合名称
	 */
	public static final String C_NAME = "C_NAME";

	public static final String FIELD_ID = "_id";

	public static final String FIELD_RTT = "rtt";

	public static final String FIELD_MAX = "max";

	public static final String FIELD_TIMES = "times";

	public static final String FIELD_CAUSE = "cause";

	public static final String FIELD_DATAS = "datas";

	public static final String FIELD_HOSTS = "hosts";

	public static final String FIELD_TOTAL = "total";

	public static final String FIELD_TRACE = "trace";

	public static final String FIELD_STATUS = "status";

	public static final String FIELD_METHOD = "method";

	public static final String FIELD_FAILED = "failed";

	public static final String FIELD_CURRENT = "current";

	public static final String FIELD_VERSION = "version";

	public static final String FIELD_SERVICE = "service";

	public static final String FIELD_SERVICES = "services";

	public static final String FIELD_TIMEOUT = "timeout";

	public static final String FIELD_EXCEPTION = "exception";

	public static final String FIELD_DEPENDENCY = "dependency";

	public static final String FIELD_HOST_LOCAL = "host_local";

	public static final String FIELD_HOST_LOCAL_SID = "host_local_sid";

	public static final String FIELD_HOST_LOCAL_PID = "host_local_pid";

	public static final String FIELD_HOST_LOCAL_TAG = "host_local_tag";

	public static final String FIELD_HOST_LOCAL_NAME = "host_local_name";

	public static final String FIELD_HOST_LOCAL_GROUP = "host_local_group";

	public static final String FIELD_HOST_TARGET = "host_target";

	public static final String FIELD_HOST_TARGET_SID = "host_target_sid";

	public static final String FIELD_HOST_TARGET_PID = "host_source_pid";

	public static final String FIELD_HOST_TARGET_TAG = "host_source_tag";

	public static final String FIELD_HOST_TARGET_GROUP = "host_target_group";

	public static final String FIELD_PERIOD = "period";

	public static final String FIELD_PERIOD_DAY = "day";

	public static final String FIELD_PERIOD_HOUR = "hour";

	public static final String FIELD_PERIOD_MINUTE = "minute";

	public static final String FIELD_PERIOD_SECOND = "second";

	public static final String FIELD_PERIOD_INTERVAL = "period_interval";

	public Object get(String key);
}
