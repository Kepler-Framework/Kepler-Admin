package com.kepler.admin.collector;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.kepler.config.PropertiesUtils;
import com.kepler.main.Prepare;
import com.kepler.org.apache.commons.lang.StringUtils;
import com.kepler.zookeeper.ZkFactory;

/**
 * 如果没有指定ZooKeeper服务器地址,提示输入
 * 
 * @author kim
 *
 * 2016年3月1日
 */
public class ZKPrepare implements Prepare {

	@Override
	public void prepare() throws Exception {
		// 不使用ZKFactory常量避免触发初始化
		String address = ZkFactory.class.getName().toLowerCase() + ".host";
		if (StringUtils.isEmpty(PropertiesUtils.get(address))) {
			System.out.println("Input ZooKeeper Address: >>");
			// Could not close System.in
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.setProperty(address, input.readLine().trim());
		}
	}
}
