package com.kepler.admin.statistics;

import java.util.HashMap;
import java.util.Map;

import com.kepler.admin.domain.Period;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.config.PropertiesUtils;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * @author kim 2015年12月21日
 */
public abstract class Statistics {

	/**
	 * 周期范围最大值, 防止过大数据造成的OOM
	 */
	private static final int MAX = PropertiesUtils.get(Statistics.class.getName().toLowerCase() + ".max", 60 * 24 * 15);

	/**
	 * 周期对应数据源
	 */
	protected final Map<Period, MongoConfig> configs = new HashMap<Period, MongoConfig>();

	protected void configs(MongoConfig transferDay, MongoConfig transferHour, MongoConfig transferMinute) {
		// 数据源
		this.configs.put(Period.DAY, transferDay);
		this.configs.put(Period.HOUR, transferHour);
		this.configs.put(Period.MINUTE, transferMinute);
	}

	/**
	 * SID + Service + Version + 周期偏移
	 * 
	 * @param sid
	 * @param service
	 * @param versionAndCatalog
	 * @param period
	 * @param offset
	 * @param length
	 * @return
	 */
	protected DBObject condition(String sid, String service, String versionAndCatalog, Period period, int offset, int length) {
		BasicDBObjectBuilder condition = this.period(BasicDBObjectBuilder.start(), period, offset, length);
		// 附加条件服务 版本
		DBObject condition_service = BasicDBObjectBuilder.start("$eq", service).get();
		condition.add(Dictionary.FIELD_SERVICE, condition_service);
		DBObject condition_version = BasicDBObjectBuilder.start("$eq", versionAndCatalog).get();
		condition.add(Dictionary.FIELD_VERSION, condition_version);
		// 附加条件SID
		condition.add(Dictionary.FIELD_HOST_TARGET_SID, BasicDBObjectBuilder.start("$eq", sid).get());
		return BasicDBObjectBuilder.start().add("$match", condition.get()).get();
	}

	/**
	 * Service + Version + 周期偏移
	 *  
	 * @param service
	 * @param versionAndCatalog
	 * @param period
	 * @param offset
	 * @param length 
	 * @return
	 */
	protected DBObject condition(String service, String versionAndCatalog, Period period, int offset, int length) {
		BasicDBObjectBuilder condition = this.period(BasicDBObjectBuilder.start(), period, offset, length);
		// 附加条件服务 版本
		DBObject condition_service = BasicDBObjectBuilder.start("$eq", service).get();
		condition.add(Dictionary.FIELD_SERVICE, condition_service);
		DBObject condition_version = BasicDBObjectBuilder.start("$eq", versionAndCatalog).get();
		condition.add(Dictionary.FIELD_VERSION, condition_version);
		return BasicDBObjectBuilder.start().add("$match", condition.get()).get();
	}

	/**
	 * 添加周期条件
	 * 
	 * @param period 周期单位
	 * @param offset 偏移
	 * @param length 长度
	 * @return
	 */
	protected BasicDBObjectBuilder period(BasicDBObjectBuilder condition, Period period, int offset, int length) {
		// 时间偏移量 = 当前周期 - 指定偏移量
		long offset_start = period.period() - offset;
		// 对跨度范围进行限制
		long offset_end = offset_start + Math.min(length, Statistics.MAX);
		DBObject condition_period = BasicDBObjectBuilder.start().add("$gte", offset_start).add("$lte", offset_end).get();
		condition.add(Dictionary.FIELD_PERIOD, condition_period);
		return condition;
	}

	/**
	 * 分组维度(仅创建一次)
	 * 
	 * @return
	 */
	protected static DBObject group(DBObject condition) {
		BasicDBObjectBuilder query = BasicDBObjectBuilder.start("_id", condition);
		// 统计(Sum)
		query.add(Dictionary.FIELD_MAX, BasicDBObjectBuilder.start("$max", "$" + Dictionary.FIELD_MAX).get());
		query.add(Dictionary.FIELD_RTT, BasicDBObjectBuilder.start("$sum", "$" + Dictionary.FIELD_RTT).get());
		query.add(Dictionary.FIELD_TOTAL, BasicDBObjectBuilder.start("$sum", "$" + Dictionary.FIELD_TOTAL).get());
		query.add(Dictionary.FIELD_TIMEOUT, BasicDBObjectBuilder.start("$sum", "$" + Dictionary.FIELD_TIMEOUT).get());
		query.add(Dictionary.FIELD_EXCEPTION, BasicDBObjectBuilder.start("$sum", "$" + Dictionary.FIELD_EXCEPTION).get());
		return BasicDBObjectBuilder.start().add("$group", query.get()).get();
	}

	/**
	 * 属性映射(仅创建一次)
	 * 
	 * @return
	 */
	protected static DBObject project(DBObject query) {
		// 统计值
		query.put(Dictionary.FIELD_MAX, "$" + Dictionary.FIELD_MAX);
		query.put(Dictionary.FIELD_RTT, "$" + Dictionary.FIELD_RTT);
		query.put(Dictionary.FIELD_TOTAL, "$" + Dictionary.FIELD_TOTAL);
		query.put(Dictionary.FIELD_TIMEOUT, "$" + Dictionary.FIELD_TIMEOUT);
		query.put(Dictionary.FIELD_EXCEPTION, "$" + Dictionary.FIELD_EXCEPTION);
		return BasicDBObjectBuilder.start().add("$project", query).get();
	}
}
