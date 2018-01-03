package com.kepler.admin.resource;

import java.util.Map;
import java.util.TreeMap;

/**
 * 排序Map, 包装Map为TreeMap并Trim key
 * 
 * @author kim
 *
 * 2016年3月7日
 */
public class OrderedMap extends TreeMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public OrderedMap(Map<String, ?> map) {
		super(String.CASE_INSENSITIVE_ORDER);
		for (String key : map.keySet()) {
			super.put(key.trim(), map.get(key));
		}
	}
}