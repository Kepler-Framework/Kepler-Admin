package com.kepler.admin.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;

import com.kepler.admin.resource.impl.Connector;
import com.kepler.config.PropertiesUtils;
import com.kepler.zookeeper.ZkFactory;

/**
 * Curator工厂 
 * 
 * @author kim 2015年12月17日
 */
public class CuratorFactory implements FactoryBean<CuratorFramework> {

	private static final int TIMES = PropertiesUtils.get(Connector.class.getName().toLowerCase() + ".times", 10);

	/**
	 * Curator基础重连间隔(指数回退)
	 */
	private static final int INTERVAL = PropertiesUtils.get(Connector.class.getName().toLowerCase() + ".retry", 5000);

	/**
	 * Curator Session超时
	 */
	private static final int TIMEOUT_SESSION = PropertiesUtils.get(Connector.class.getName().toLowerCase() + ".timeout_session", 30000);

	/**
	 * Curator Connection超时
	 */
	private static final int TIMEOUT_CONNECTION = PropertiesUtils.get(Connector.class.getName().toLowerCase() + ".timeout_connection", 120000);

	private final CuratorFramework client = CuratorFrameworkFactory.newClient(ZkFactory.HOST, CuratorFactory.TIMEOUT_SESSION, CuratorFactory.TIMEOUT_CONNECTION, new ExponentialBackoffRetry(CuratorFactory.INTERVAL, CuratorFactory.TIMES));

	@Override
	public CuratorFramework getObject() throws Exception {
		this.client.start();
		return this.client;
	}

	public void destroy() {
		this.client.close();
	}

	@Override
	public Class<?> getObjectType() {
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
