package com.kepler.admin.collector;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.kepler.config.PropertiesUtils;
import com.kepler.main.Prepare;
import com.kepler.org.apache.commons.lang.StringUtils;
import com.mongodb.MongoClient;

/**
 * 如果没有指定Mongo服务器地址,提示输入
 * 
 * @author kim
 *
 * 2016年3月1日
 */
public class MongoPrepare implements Prepare {

	@Override
	public void prepare() throws Exception {
		String address = MongoClient.class.getName().toLowerCase() + ".address";
		if (StringUtils.isEmpty(PropertiesUtils.get(address))) {
			System.out.println("Input MongoDB Address: >>");
			// Could not close System.in
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.setProperty(address, input.readLine().trim());
		}
	}
}
