package com.kepler.admin.collector.transfer;

import java.util.Collection;

import com.kepler.admin.domain.Period;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.transfer.Feeder;
import com.kepler.admin.transfer.Transfer;
import com.kepler.admin.transfer.Transfers;
import com.kepler.annotation.Autowired;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBObject;

/**
 * 分时维度
 * 
 * @author kim 2015年7月22日
 */
@Autowired(version = { "0.0.1", "0.0.7", "0.0.8" })
public class TransferHandler implements Feeder {

	// Period, Service ,Version, Host_local_sid, Host_target_sid, Host_local_sid, Method
	private static final DBObject INDEX = BasicDBObjectBuilder.start().add(Dictionary.FIELD_PERIOD, 1).add(Dictionary.FIELD_SERVICE, 1).add(Dictionary.FIELD_VERSION, 1).add(Dictionary.FIELD_HOST_TARGET_SID, 1).add(Dictionary.FIELD_HOST_LOCAL_SID, 1).add(Dictionary.FIELD_METHOD, 1).get();

	private final MongoConfig transfers4minute;

	private final MongoConfig transfers4hour;

	private final MongoConfig transfers4day;

	public TransferHandler(MongoConfig transfers4minute, MongoConfig transfers4hour, MongoConfig transfers4day) {
		super();
		this.transfers4minute = transfers4minute;
		this.transfers4hour = transfers4hour;
		this.transfers4day = transfers4day;
	}

	public void init() {
		this.transfers4minute.collection().index(TransferHandler.INDEX);
		this.transfers4hour.collection().index(TransferHandler.INDEX);
		this.transfers4day.collection().index(TransferHandler.INDEX);
	}

	/**
	 * 更新Max值(覆盖)
	 * 
	 * @param transfer
	 * @return
	 */
	private DBObject update4max(Transfer transfer) {
		BasicDBObjectBuilder set = BasicDBObjectBuilder.start();
		set.add(Dictionary.FIELD_MAX, transfer.max());
		return BasicDBObjectBuilder.start().add("$set", set.get()).get();
	}

	/**
	 * 更新RTT, TOTAL, TIMEOUT, EXCEPTION(递增), 并初始化MAX
	 * 
	 * @param transfer
	 * @return
	 */
	private DBObject update4transfer(Transfer transfer) {
		BasicDBObjectBuilder inc = BasicDBObjectBuilder.start();
		inc.add(Dictionary.FIELD_RTT, transfer.rtt());
		inc.add(Dictionary.FIELD_TOTAL, transfer.total());
		inc.add(Dictionary.FIELD_TIMEOUT, transfer.timeout());
		inc.add(Dictionary.FIELD_EXCEPTION, transfer.exception());
		return BasicDBObjectBuilder.start().add("$inc", inc.get()).add("$setOnInsert", BasicDBObjectBuilder.start(Dictionary.FIELD_MAX, 0).get()).get();
	}

	/**
	 * Service / Version / Method / Transfer
	 * 
	 * @param transfers
	 * @param max
	 * @return
	 */
	private DBObject query4max(Transfers transfers, Transfer transfer) {
		// Query (Service, Version, Method)
		DBObject query = this.query(transfers);
		// Target - Local
		query.put(Dictionary.FIELD_HOST_TARGET_SID, transfer.target().sid());
		query.put(Dictionary.FIELD_HOST_LOCAL_SID, transfer.local().sid());
		// 并且MAX值小于当前值
		query.put(Dictionary.FIELD_MAX, BasicDBObjectBuilder.start("$lt", transfer.max()).get());
		return query;
	}

	/**
	 * Service / Version / Method
	 * 
	 * @param transfers
	 * @return
	 */
	private DBObject query(Transfers transfers) {
		// Query (Service, Version, Method)
		BasicDBObjectBuilder query = BasicDBObjectBuilder.start();
		query.add(Dictionary.FIELD_SERVICE, transfers.service());
		query.add(Dictionary.FIELD_VERSION, transfers.version());
		query.add(Dictionary.FIELD_METHOD, transfers.method());
		return query.get();
	}

	@Override
	public void feed(Collection<Transfers> transfers) {
		// 开启Batch
		BulkWriteOperation batch4minute = this.transfers4minute.collection().bulkWrite();
		BulkWriteOperation batch4hour = this.transfers4hour.collection().bulkWrite();
		BulkWriteOperation batch4day = this.transfers4day.collection().bulkWrite();
		// 提交并产生任务后执行批量
		this.submit4transfer(transfers, batch4minute, batch4hour, batch4day);
		batch4minute.execute();
		batch4hour.execute();
		batch4day.execute();
	}

	/**
	 * 提交批量任务
	 * 
	 * @param timestamp
	 * @param transfers
	 * @param batch4minute
	 * @param batch4hour
	 * @param batch4day
	 * @return
	 */
	private void submit4transfer(Collection<Transfers> transfers, BulkWriteOperation batch4minute, BulkWriteOperation batch4hour, BulkWriteOperation batch4day) {
		for (Transfers each_transfers : transfers) {
			DBObject query_transfer = this.query(each_transfers);
			for (Transfer each_transfer : each_transfers.transfers()) {
				// Target - Local
				query_transfer.put(Dictionary.FIELD_HOST_TARGET_SID, each_transfer.target().sid());
				query_transfer.put(Dictionary.FIELD_HOST_LOCAL_SID, each_transfer.local().sid());
				DBObject query_max = this.query4max(each_transfers, each_transfer);
				DBObject update_transfer = this.update4transfer(each_transfer);
				DBObject update_max = this.update4max(each_transfer);
				// Update
				batch4minute.find(BasicDBObjectBuilder.start(query_transfer.toMap()).add(Dictionary.FIELD_PERIOD, Period.MINUTE.period(each_transfer.timestamp())).get()).upsert().updateOne(update_transfer);
				batch4hour.find(BasicDBObjectBuilder.start(query_transfer.toMap()).add(Dictionary.FIELD_PERIOD, Period.HOUR.period(each_transfer.timestamp())).get()).upsert().updateOne(update_transfer);
				batch4day.find(BasicDBObjectBuilder.start(query_transfer.toMap()).add(Dictionary.FIELD_PERIOD, Period.DAY.period(each_transfer.timestamp())).get()).upsert().updateOne(update_transfer);
				batch4minute.find(BasicDBObjectBuilder.start(query_max.toMap()).add(Dictionary.FIELD_PERIOD, Period.MINUTE.period(each_transfer.timestamp())).get()).upsert().updateOne(update_max);
				batch4hour.find(BasicDBObjectBuilder.start(query_max.toMap()).add(Dictionary.FIELD_PERIOD, Period.HOUR.period(each_transfer.timestamp())).get()).upsert().updateOne(update_max);
				batch4day.find(BasicDBObjectBuilder.start(query_max.toMap()).add(Dictionary.FIELD_PERIOD, Period.DAY.period(each_transfer.timestamp())).get()).upsert().updateOne(update_max);
			}
		}
	}
}
