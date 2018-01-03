package com.kepler.admin.statistics.barchart.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kepler.admin.domain.Period;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.mongo.impl.MongoUtils;
import com.kepler.admin.resource.terminal.TerminalStatusFinder;
import com.kepler.admin.statistics.Statistics;
import com.kepler.admin.statistics.barchart.BarChartData;
import com.kepler.admin.statistics.barchart.BarChartSeries;
import com.kepler.admin.statistics.barchart.BarChatService;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * @author longyaokun 2016年3月21日
 *
 */
public class BarChartServiceImpl extends Statistics implements BarChatService {

	private static final DBObject PROJECT = Statistics.project(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$_id." + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$_id." + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_HOST_LOCAL_SID, "$_id." + Dictionary.FIELD_HOST_LOCAL_SID).get());

	private static final DBObject GROUP = Statistics.group(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$" + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$" + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_HOST_LOCAL_SID, "$" + Dictionary.FIELD_HOST_LOCAL_SID).get());

	private static final Log LOGGER = LogFactory.getLog(BarChartServiceImpl.class);

	private final TerminalStatusFinder finder;

	public BarChartServiceImpl(MongoConfig transferDay, MongoConfig transferHour, MongoConfig transferMinute, TerminalStatusFinder finder) {
		super();
		super.configs(transferDay, transferHour, transferMinute);
		this.finder = finder;
	}

	private DBObject match(String service, String versionAndCatalog, String method, Period period, int offset, int length) {
		// 基础维度
		BasicDBObjectBuilder condition = BasicDBObjectBuilder.start();
		condition.add(Dictionary.FIELD_SERVICE, service);
		condition.add(Dictionary.FIELD_VERSION, versionAndCatalog);
		condition.add(Dictionary.FIELD_METHOD, method);
		// 附加条件 查询周期
		condition = super.period(condition, period, offset, length);
		return BasicDBObjectBuilder.start().add("$match", condition.get()).get();
	}

	@Override
	public List<BarChartData> statistics(String service, String versionAndCatalog, String method, Period period, int offset, int length) {
		return new MongoDataSet(super.configs.get(period).collection().aggregate(this.match(service, versionAndCatalog, method, period, offset, length), BarChartServiceImpl.GROUP, BarChartServiceImpl.PROJECT));
	}

	/**
	 * 结果数据结构
	 * 
	 * @author KimShen
	 *
	 */
	private class MongoDataSet extends ArrayList<BarChartData> {

		private static final long serialVersionUID = 1L;

		/**
		 * 耗时
		 */
		private final BarChartData elapse = new BarChartData();

		/**
		 * 异常
		 */
		private final BarChartData error = new BarChartData();

		/**
		 * 访问量
		 */
		private final BarChartData pv = new BarChartData();

		private final List<Double> elapses = new ArrayList<Double>();

		private final List<Long> errors = new ArrayList<Long>();

		private final List<Long> pvs = new ArrayList<Long>();

		private MongoDataSet(AggregationOutput output) {
			super();
			// 检查数据
			if (output != null && output.results() != null) {
				Iterator<DBObject> iterator = output.results().iterator();
				while (iterator.hasNext()) {
					try {
						DBObject current = iterator.next();
						// 获取主机信息
						String host = BarChartServiceImpl.this.finder.sid(MongoUtils.asString(current, Dictionary.FIELD_HOST_LOCAL_SID)).getHost();
						this.pv.addCategory(host);
						this.error.addCategory(host);
						this.elapse.addCategory(host);
						long total = MongoUtils.asLong(current, Dictionary.FIELD_TOTAL);
						this.pvs.add(total);
						this.errors.add(MongoUtils.asLong(current, Dictionary.FIELD_EXCEPTION) + MongoUtils.asLong(current, Dictionary.FIELD_TIMEOUT));
						// 当前周期总耗时 / 当前周期总访问量
						this.elapses.add((total == 0 ? 0 : Double.valueOf(new DecimalFormat("#.00").format(MongoUtils.asDouble(current, Dictionary.FIELD_RTT, 0) / total))));
					} catch (Exception e) {
						// 脏数据捕捉
						BarChartServiceImpl.LOGGER.warn(e.getMessage(), e);
					}
				}
				this.pv.addSeries(new BarChartSeries("pv", this.pvs.toArray()));
				this.error.addSeries(new BarChartSeries("error", this.errors.toArray()));
				this.elapse.addSeries(new BarChartSeries("elapse", this.elapses.toArray()));
				// 强顺序
				super.add(this.pv);
				super.add(this.error);
				super.add(this.elapse);
			}
		}
	}

}
