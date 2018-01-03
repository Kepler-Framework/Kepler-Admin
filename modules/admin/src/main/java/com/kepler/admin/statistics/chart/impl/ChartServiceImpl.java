package com.kepler.admin.statistics.chart.impl;

import java.text.DecimalFormat;
import java.util.Iterator;

import com.kepler.admin.domain.Period;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.mongo.impl.MongoUtils;
import com.kepler.admin.statistics.Statistics;
import com.kepler.admin.statistics.chart.ChartDataset;
import com.kepler.admin.statistics.chart.ChartService;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * @author longyaokun 2015年12月21日
 */
public class ChartServiceImpl extends Statistics implements ChartService {

	private static final DBObject PROJECT = Statistics.project(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$_id." + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$_id." + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_PERIOD, "$_id." + Dictionary.FIELD_PERIOD).get());

	private static final DBObject GROUP = Statistics.group(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$" + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$" + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_PERIOD, "$" + Dictionary.FIELD_PERIOD).get());

	private static final DBObject SORT = BasicDBObjectBuilder.start().add("$sort", BasicDBObjectBuilder.start(Dictionary.FIELD_PERIOD, 1).get()).get();

	public ChartServiceImpl(MongoConfig transferDay, MongoConfig transferHour, MongoConfig transferMinute) {
		super();
		super.configs(transferDay, transferHour, transferMinute);
	}

	@Override
	public ChartDataset service(String service, String versionAndCatalog, Period period, int offset, int length) {
		DBObject match = super.condition(service, versionAndCatalog, period, offset, length);
		// 聚合数据
		AggregationOutput aggregate = super.configs.get(period).collection().aggregate(match, ChartServiceImpl.GROUP, ChartServiceImpl.PROJECT, ChartServiceImpl.SORT);
		return new MongoDataset(service + "-" + versionAndCatalog, period, aggregate);
	}

	@Override
	public ChartDataset instance(String sid, String service, String versionAndCatalog, Period period, int offset, int length) {
		DBObject match = super.condition(sid, service, versionAndCatalog, period, offset, length);
		// 聚合数据
		AggregationOutput aggregate = super.configs.get(period).collection().aggregate(match, ChartServiceImpl.GROUP, ChartServiceImpl.PROJECT, ChartServiceImpl.SORT);
		return new MongoDataset(service + "-" + versionAndCatalog, period, aggregate);
	}

	private class MongoDataset extends ChartDataset {

		private MongoDataset(String title, Period period, AggregationOutput output) {
			super(title);
			// 检查数据
			if (output != null && output.results() != null) {
				Iterator<DBObject> iterator = output.results().iterator();
				while (iterator.hasNext()) {
					DBObject object = iterator.next();
					// 周期转换为相关单位值
					long time = period.convert(MongoUtils.asLong(object, Dictionary.FIELD_PERIOD));
					// 访问总量
					long total = MongoUtils.asLong(object, Dictionary.FIELD_TOTAL);
					super.total(new Object[] { time, total });
					// 错误 = 异常 + 超时
					super.error(new Object[] { time, MongoUtils.asLong(object, Dictionary.FIELD_EXCEPTION) + MongoUtils.asLong(object, Dictionary.FIELD_TIMEOUT) });
					super.elapse(new Object[] { time, (total == 0 ? 0 : Double.valueOf(new DecimalFormat("#.00").format(MongoUtils.asDouble(object, Dictionary.FIELD_RTT, 0) / total))) });
				}
			}
		}
	}
}
