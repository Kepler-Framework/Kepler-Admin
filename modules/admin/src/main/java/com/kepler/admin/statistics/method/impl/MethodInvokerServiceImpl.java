package com.kepler.admin.statistics.method.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import com.kepler.admin.domain.Period;
import com.kepler.admin.domain.ServiceAndVersion;
import com.kepler.admin.mongo.Dictionary;
import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.mongo.impl.MongoUtils;
import com.kepler.admin.resource.instance.InstanceFinder;
import com.kepler.admin.resource.instance.impl.InstanceServices;
import com.kepler.admin.statistics.Statistics;
import com.kepler.admin.statistics.method.MethodInvoker;
import com.kepler.admin.statistics.method.MethodInvokerService;
import com.kepler.admin.statistics.method.SortBy;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * @author longyaokun 2015年12月21日
 */
public class MethodInvokerServiceImpl extends Statistics implements MethodInvokerService {

	private static final DBObject PROJECT_INSTANCE = Statistics.project(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$_id." + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$_id." + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_HOST_TARGET_SID, "$_id." + Dictionary.FIELD_HOST_TARGET_SID).add(Dictionary.FIELD_METHOD, "$_id." + Dictionary.FIELD_METHOD).add(Dictionary.FIELD_HOST_TARGET_SID, "$_id." + Dictionary.FIELD_HOST_TARGET_SID).get());

	private static final DBObject PROJECT_SERVICE = Statistics.project(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$_id." + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$_id." + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_HOST_TARGET_SID, "$_id." + Dictionary.FIELD_HOST_TARGET_SID).add(Dictionary.FIELD_METHOD, "$_id." + Dictionary.FIELD_METHOD).get());

	private static final DBObject GROUP_INSTANCE = Statistics.group(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$" + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$" + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_METHOD, "$" + Dictionary.FIELD_METHOD).add(Dictionary.FIELD_HOST_TARGET_SID, "$" + Dictionary.FIELD_HOST_TARGET_SID).get());

	private static final DBObject GROUP_SERVICE = Statistics.group(BasicDBObjectBuilder.start().add(Dictionary.FIELD_SERVICE, "$" + Dictionary.FIELD_SERVICE).add(Dictionary.FIELD_VERSION, "$" + Dictionary.FIELD_VERSION).add(Dictionary.FIELD_METHOD, "$" + Dictionary.FIELD_METHOD).get());

	private final InstanceFinder instance;

	public MethodInvokerServiceImpl(MongoConfig transferDay, MongoConfig transferHour, MongoConfig transferMinute, InstanceFinder instance) {
		super();
		super.configs(transferDay, transferHour, transferMinute);
		this.instance = instance;
	}

	@Override
	public List<MethodInvoker> methods4service(String service, String versionAndCatalog, Period period, int offset, int length) {
		DBObject condition = super.condition(service, versionAndCatalog, period, offset, length);
		return new MongoMethods(offset, super.configs.get(period).collection().aggregate(condition, MethodInvokerServiceImpl.GROUP_SERVICE, MethodInvokerServiceImpl.PROJECT_SERVICE));
	}

	@Override
	public List<MethodInvoker> methods(String sid, String service, String versionAndCatalog, Period period, int offset, int length) {
		DBObject condition = super.condition(sid, service, versionAndCatalog, period, offset, length);
		return new MongoMethods(offset, super.configs.get(period).collection().aggregate(condition, MethodInvokerServiceImpl.GROUP_INSTANCE, MethodInvokerServiceImpl.PROJECT_INSTANCE));
	}

	@Override
	public List<MethodInvoker> methods4group(String group, String application, Period period, int offset, int length, SortBy sort) {
		List<MethodInvoker> methods4group = new ArrayList<>();
		// 获取分组所有服务, 如果没有传递application则查询整个分组
		for (ServiceAndVersion serviceAndVersion : new InstanceServices(StringUtils.isEmpty(application) ? this.instance.group(group) : this.instance.application(group, application))) {
			List<MethodInvoker> methods4Service = this.methods4service(serviceAndVersion.getService(), serviceAndVersion.getVersionAndCatalog(), period, offset, length);
			methods4group.addAll(methods4Service);
		}
		// 内存排序
		Collections.sort(methods4group, sort.comparator());
		return methods4group;
	}

	private class MongoMethods extends ArrayList<MethodInvoker> {

		private static final long serialVersionUID = 1L;

		private MongoMethods(int offset, AggregationOutput output) {
			// 数据检查
			if (output != null && output.results() != null) {
				Iterator<DBObject> iterator = output.results().iterator();
				while (iterator.hasNext()) {
					DBObject object = iterator.next();
					String service = MongoUtils.asString(object, Dictionary.FIELD_SERVICE);
					String version = MongoUtils.asString(object, Dictionary.FIELD_VERSION);
					String name = MongoUtils.asString(object, Dictionary.FIELD_METHOD);
					long exception = MongoUtils.asLong(object, Dictionary.FIELD_EXCEPTION);
					long timeout = MongoUtils.asLong(object, Dictionary.FIELD_TIMEOUT);
					long total = MongoUtils.asLong(object, Dictionary.FIELD_TOTAL, 0);
					long max = MongoUtils.asLong(object, Dictionary.FIELD_MAX, 0);
					double rtt = total == 0 ? 0 : Double.valueOf(new DecimalFormat("#.00").format(MongoUtils.asDouble(object, Dictionary.FIELD_RTT, 0) / total));
					super.add(new MethodInvoker(service, version, name, total, timeout, exception, max, rtt));
				}
			}
		}
	}
}
