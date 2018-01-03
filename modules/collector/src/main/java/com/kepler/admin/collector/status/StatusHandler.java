package com.kepler.admin.collector.status;

import java.util.Map;

import com.kepler.admin.domain.Period;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.status.Feeder;
import com.kepler.admin.status.Point;
import com.kepler.annotation.Autowired;
import com.kepler.host.Host;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * 动态状态收集
 * 
 * @author kim 2015年7月22日
 */
@Autowired(version = { "0.0.1", "0.0.7", "0.0.8" })
public class StatusHandler implements Feeder {

	/**
	 * 索引, Peroid - SID
	 */
	private static final DBObject INDEX = BasicDBObjectBuilder.start().add(Dictionary.FIELD_PERIOD, 1).add(Dictionary.FIELD_HOST_LOCAL_SID, 1).get();

	private final MongoConfig status;

	public StatusHandler(MongoConfig status) {
		super();
		this.status = status;
	}

	public void init() {
		this.status.collection().index(StatusHandler.INDEX);
	}

	@Override
	public void feed(long timestamp, Host local, Map<String, Point> status) {
		// Period(秒) - SID(对应索引)
		BasicDBObjectBuilder insert = BasicDBObjectBuilder.start().add(Dictionary.FIELD_PERIOD, Period.SECOND.period(timestamp)).add(Dictionary.FIELD_HOST_LOCAL_SID, local.sid());
		for (String field : status.keySet()) {
			Object value = status.get(field);
			if (Point.class.isAssignableFrom(value.getClass())) {
				this.point(insert, field, value);
			} else {
				insert.add(field, value);
			}
		}
		this.status.collection().save(insert.get());
	}

	/**
	 * 构建Point数据
	 * 
	 * @param insert
	 * @param field
	 * @param value
	 */
	private void point(BasicDBObjectBuilder insert, String field, Object value) {
		Point point = Point.class.cast(value);
		insert.add(field, BasicDBObjectBuilder.start().add(Dictionary.FIELD_TIMES, point.times()).add(Dictionary.FIELD_DATAS, point.datas()).get());
	}
}
