package com.kepler.admin.traces.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.xerial.snappy.Snappy;

import com.kepler.admin.mongo.MongoConfig;
import com.kepler.admin.mongo.impl.MongoUtils;
import com.kepler.admin.traces.TraceService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author zhangjiehao
 *
 */
public class TraceServiceImpl implements TraceService {

	private static final Log LOGGER = LogFactory.getLog(TraceService.class);

	private static final String FIELD_USE_SNAPPY = "useSnappy";

	private static final String FIELD_START_TIME = "startTime";

	private static final String FIELD_CHILDREN = "children";

	private static final String FIELD_RESPONSE = "response";

	private static final String FIELD_REQUEST = "request";

	private static final String FIELD_SPAN_PARENT = "parentSpan";

	private static final String FIELD_SPAN = "span";

	private static final String FIELD_TRACE = "trace";

	private final SpanComparator comparator = new SpanComparator();

	private final MongoConfig mongo;

	public TraceServiceImpl(MongoConfig mongo) {
		this.mongo = mongo;
	}

	@Override
	public List<?> trace(String traceId) {
		// span_map, 保存所有树节点. Key:Trace Parent Value:相关节点集合
		Map<String, List<DBObject>> span_map = new HashMap<>();
		// span_root, 保存所有Root节点
		List<DBObject> span_root = new ArrayList<DBObject>();
		// 获取Trace链路
		for (DBObject node : this.mongo.collection().find(new BasicDBObject(TraceServiceImpl.FIELD_TRACE, traceId)).toArray()) {
			// 解压(如果需要)
			this.decode(node);
			String parent = MongoUtils.asString(node, TraceServiceImpl.FIELD_SPAN_PARENT);
			// 加载Root, 如果不存在Parent则作为Root节点加载(多个)
			if (parent == null) {
				span_root.add(node);
				continue;
			}
			// 如果不为Root, 则挂在到对应Trace Parent的节点集合
			this.build4nodes(span_map, node, parent);
		}
		return CollectionUtils.isEmpty(span_root) ? null : this.build4tree(span_root, span_map);
	}

	/**
	 * 构建树节点数据
	 * 
	 * @param span_map 保存所有树节点. Key:Trace Parent Value:相关节点集合
	 * @param node 当前节点
	 * @param parent Trace Parent
	 */
	private void build4nodes(Map<String, List<DBObject>> span_map, DBObject node, String parent) {
		// 获取指定Parent的Children并加载
		List<DBObject> children = span_map.get(parent);
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(node);
		span_map.put(parent, children);
	}

	/**
	 * 构建树
	 * 
	 * @param span_root Root节点集合
	 * @param span_map 保存所有树节点. Key:Trace Parent Value:相关节点集合
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<?> build4tree(List<DBObject> span_root, Map<String, List<DBObject>> span_map) {
		List<Map> tree = new ArrayList<Map>();
		for (DBObject root : span_root) {
			// 构建子树
			this.build4children(root, span_map);
			tree.add(root.toMap());
		}
		return tree;
	}

	/**
	 * 构建子树
	 * 
	 * @param root Root节点
	 * @param span_map 保存所有树节点. Key:Trace Parent Value:相关节点集合
	 */
	private void build4children(DBObject root, Map<String, List<DBObject>> span_map) {
		// 获取属于该SPAN的所有节点
		List<DBObject> spans = span_map.get(MongoUtils.asString(root, TraceServiceImpl.FIELD_SPAN));
		// Guard case, 如果Span树为空则直接返回
		if (spans == null) {
			return;
		}
		// 构建Span树并加入Root节点
		List<DBObject> children = new ArrayList<>();
		root.put(TraceServiceImpl.FIELD_CHILDREN, children);
		for (DBObject span : spans) {
			// 递归构建
			this.build4children(span, span_map);
			children.add(span);
		}
		// 节点排序
		Collections.sort(children, this.comparator);
	}

	/**
	 * 解压
	 * 
	 * @param span
	 */
	private void decode(DBObject span) {
		// Guard case, 不需要解压
		if (!MongoUtils.asBoolean(span, TraceServiceImpl.FIELD_USE_SNAPPY, false)) {
			return;
		}
		try {
			this.decode(TraceServiceImpl.FIELD_REQUEST, span);
			this.decode(TraceServiceImpl.FIELD_RESPONSE, span);
		} catch (Exception e) {
			TraceServiceImpl.LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 实际解压缩
	 * 
	 * @param key
	 * @param span
	 * @throws Exception
	 */
	private void decode(String key, DBObject span) throws Exception {
		if (span.get(key) != null) {
			span.put(key, Snappy.uncompressString((byte[]) span.get(key)));
		}
	}

	/**
	 * Span排序器
	 * 
	 * @author zhangjiehao
	 *
	 */
	private class SpanComparator implements Comparator<DBObject> {
		@Override
		public int compare(DBObject o1, DBObject o2) {
			long startTime1 = o1.get(FIELD_START_TIME) instanceof Date ? ((Date) o1.get(FIELD_START_TIME)).getTime() : (long) o1.get(FIELD_START_TIME);
			long startTime2 = o2.get(FIELD_START_TIME) instanceof Date ? ((Date) o2.get(FIELD_START_TIME)).getTime() : (long) o2.get(FIELD_START_TIME);
			return (int) (startTime1 - startTime2);
		}
	}
}
