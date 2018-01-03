package com.kepler.admin.statistics.chart.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kepler.admin.domain.Period;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.mongo.impl.MongoUtils;
import com.kepler.admin.resource.terminal.TerminalStatusFinder;
import com.kepler.admin.statistics.chart.StatusDataset;
import com.kepler.admin.statistics.chart.StatusService;
import com.kepler.config.PropertiesUtils;
import com.kepler.org.apache.commons.lang.reflect.MethodUtils;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author longyaokun
 * 
 */
public class StatusServiceImpl implements StatusService {

	// 默认偏移量, 5分钟
	private static final int OFFSET = PropertiesUtils.get(StatusServiceImpl.class.getName().toLowerCase() + ".offset", 60 * 5);

	private static final DBObject SORT = BasicDBObjectBuilder.start(Dictionary.FIELD_PERIOD, 1).get();

	private static final Log LOGGER = LogFactory.getLog(StatusServiceImpl.class);

	private final TerminalStatusFinder finder;

	private final MongoConfig status;

	public StatusServiceImpl(MongoConfig status, TerminalStatusFinder finder) {
		this.status = status;
		this.finder = finder;
	}

	/**
	 * 计算时间偏移量(指定时间转换为周期(秒)后减去偏移量)
	 * 
	 * @param date 原始时间
	 * @param offset 时间偏移
	 * @return
	 */
	private long offset(Date date, int offset) {
		return Period.SECOND.period(date != null ? date.getTime() : System.currentTimeMillis()) - offset;
	}

	@Override
	public StatusDataset status(String sid, Date start, Date end) {
		/**
		 * 索引, Peroid - SID
		 */
		BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
		query.add(Dictionary.FIELD_PERIOD, BasicDBObjectBuilder.start().add("$gte", this.offset(start, StatusServiceImpl.OFFSET)).add("$lte", this.offset(end, 0)).get());
		query.add(Dictionary.FIELD_HOST_LOCAL_SID, sid);
		// 按周期排序
		return new MongoDataset(sid, this.status.collection().find(query.get()).sort(StatusServiceImpl.SORT));
	}

	private class MongoDataset extends StatusDataset {

		@SuppressWarnings("unchecked")
		private MongoDataset(String sid, DBCursor cursor) {
			super(sid);
			try (DBCursor iterator = cursor) {
				while (iterator.hasNext()) {
					DBObject current = iterator.next();
					this.add("thread_active_jvm", "threadJvm", current);
					this.add("thread_active_framework", "threadFramework", current);
					this.add("memory_heap_max", "memoryMax", current, (1024 * 1024));
					this.add("memory_heap_used", "memoryUsed", current, (1024 * 1024));
					this.traffic(current);
					this.quality(current);
					this.gc(current, (List<String>) StatusServiceImpl.this.finder.sid(sid).getStatus().get("gc_names"));
				}
			}
		}

		/**
		 * 追加数据
		 *
		 * @param key
		 * @param method
		 * @param current
		 */
		private void add(String key, String method, DBObject current) {
			this.add(key, method, current, 1);
		}

		/**
		 * 追加数据
		 *
		 * @param key
		 * @param method
		 * @param current
		 * @param base 基数
		 */
		private void add(String key, String method, DBObject current, int base) {
			DBObject status = MongoUtils.asDBObject(current, key);
			List<Long> times = MongoUtils.asList(status, "times");
			List<Long> dates = MongoUtils.asList(status, "datas");
			for (int index = 0; index < times.size(); index++) {
				try {
					MethodUtils.invokeMethod(this, method, new Object[] { new Object[] { Period.MILLISECOND.period(times.get(index)), dates.get(index) / base } });
				} catch (Exception e) {
					StatusServiceImpl.LOGGER.error(e.getMessage(), e);
				}
			}
		}

		/**
		 * 计算质量
		 * 
		 * @param current
		 */
		private void quality(DBObject current) {
			long second = MongoUtils.asLong(current, Dictionary.FIELD_PERIOD);
			super.qualityWaiting(new Object[] { second * 1000, MongoUtils.asLong(current, "quality_waiting", 0) });
			super.qualityDemoting(new Object[] { second * 1000, MongoUtils.asLong(current, "quality_demoting", 0) });
			super.qualityBreaking(new Object[] { second * 1000, MongoUtils.asLong(current, "quality_breaking", 0) });
		}

		/**
		 * 计算流量
		 * 
		 * @param current
		 */
		private void traffic(DBObject current) {
			long second = MongoUtils.asLong(current, Dictionary.FIELD_PERIOD);
			super.trafficInput(new Object[] { second * 1000, MongoUtils.asLong(current, "traffic_input", 0) });
			super.trafficOutput(new Object[] { second * 1000, MongoUtils.asLong(current, "traffic_output", 0) });
		}

		/**
		 * 计算垃圾回收
		 * 
		 * @param current
		 * @param collector
		 */
		private void gc(DBObject current, List<String> collector) {
			for (int index = 0; index < collector.size(); index++) {
				String name = collector.get(index);
				DBObject status = MongoUtils.asDBObject(current, name);
				List<Long> times = MongoUtils.asList(status, "times");
				List<Long> dates = MongoUtils.asList(status, "datas");
				for (int each = 0; each < times.size(); each++) {
					super.gc(new Object[] { Period.MILLISECOND.period(times.get(each)), dates.get(each) }, name);
				}
			}
		}
	}
}
