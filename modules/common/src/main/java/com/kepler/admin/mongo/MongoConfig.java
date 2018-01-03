package com.kepler.admin.mongo;


/**
 * 
 * @author kim 2013-11-15
 */
public interface MongoConfig extends Dictionary {
	/**
	 * 重置数据集合
	 * 
	 * @return
	 */
	public MongoConfig reset();

	public MongoOperation collection();
}