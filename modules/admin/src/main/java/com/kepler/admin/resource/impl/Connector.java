package com.kepler.admin.resource.impl;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import com.kepler.admin.resource.instance.InstanceContext;
import com.kepler.admin.resource.instance.impl.InstanceImpl;
import com.kepler.admin.resource.terminal.TerminalStatusContext;
import com.kepler.admin.resource.terminal.impl.TerminalStatusImpl;
import com.kepler.host.HostStatus;
import com.kepler.serial.Serials;
import com.kepler.zookeeper.ZkContext;
import com.kepler.zookeeper.ZkSerial;

/**
 * Curator -> 本地Mapping桥
 * 
 * @author kim 2015年12月16日
 */
public class Connector {

	private static final Log LOGGER = LogFactory.getLog(Connector.class);

	/**
	 * 监听服务实例变化
	 */
	private final InstanceListener listener4instance = new InstanceListener();

	/**
	 * 监听主机状态变化
	 */
	private final StatusListener listener4status = new StatusListener();

	/**
	 * 对应StatusListener
	 */
	private final TerminalStatusContext context4status;

	/**
	 * 对应ServiceListener
	 */
	private final InstanceContext context4service;

	private final TreeCache cache4instance;

	private final TreeCache cache4status;

	private final Serials serials;

	public Connector(Serials serials, CuratorFramework client, ThreadPoolExecutor executor, InstanceContext context4service, TerminalStatusContext context4status) {
		super();
		this.cache4status = TreeCache.newBuilder(client, ZkContext.ROOT + ZkContext.STATUS).setExecutor(executor).setCacheData(false).build();
		this.cache4instance = TreeCache.newBuilder(client, ZkContext.ROOT).setExecutor(executor).setCacheData(false).build();
		this.context4service = context4service;
		this.context4status = context4status;
		this.serials = serials;
	}

	public void init() throws Exception {
		// 绑定并启动
		this.cache4instance.getListenable().addListener(this.listener4instance);
		this.cache4status.getListenable().addListener(this.listener4status);
		this.cache4instance.start();
		this.cache4status.start();
	}

	public void destroy() {
		// 关闭并释放
		this.cache4instance.close();
		this.cache4status.close();
	}

	/**
	 * 用于监听Status
	 * 
	 * @author KimShen
	 *
	 */
	private class StatusListener implements TreeCacheListener {

		public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
			if (event.getData() != null) {
				Connector.LOGGER.info("Status loading: " + event.getData().getPath());
				byte[] data = client.getData().forPath(event.getData().getPath());
				switch (event.getType()) {
				case NODE_ADDED:
					// 加载主机静态信息
					if (data.length != 0) {
						Connector.this.context4status.insert(new TerminalStatusImpl(event.getData().getPath(), Connector.this.serials.def4input().input(data, HostStatus.class)));
					}
					break;
				case NODE_UPDATED:
					// 更新主机静态信息
					if (data.length != 0) {
						Connector.this.context4status.update(new TerminalStatusImpl(event.getData().getPath(), Connector.this.serials.def4input().input(data, HostStatus.class)));
					}
					break;
				case NODE_REMOVED:
					// 删除主机静态信息
					Connector.this.context4status.remove(event.getData().getPath());
					break;
				default:
					Connector.LOGGER.warn("Nothing for " + event + " ... ");
				}
			}
		}
	}

	/**
	 * 用于监听服务实例
	 * 
	 * @author KimShen
	 *
	 */
	private class InstanceListener implements TreeCacheListener {

		public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
			if (event.getData() != null) {
				Connector.LOGGER.info("Instance loading: " + event.getData().getPath());
				byte[] data = client.getData().forPath(event.getData().getPath());
				switch (event.getType()) {
				case NODE_ADDED:
					// 加载服务实例
					if (data.length != 0) {
						Connector.this.context4service.insert(new InstanceImpl(event.getData().getPath(), Connector.this.serials.def4input().input(data, ZkSerial.class)));
					}
					break;
				case NODE_UPDATED:
					// 更新服务配置
					if (data.length != 0) {
						Connector.this.context4service.update(new InstanceImpl(event.getData().getPath(), Connector.this.serials.def4input().input(data, ZkSerial.class)));
					}
					break;
				case NODE_REMOVED:
					// 删除
					Connector.this.context4service.remove(event.getData().getPath());
					break;
				default:
					Connector.LOGGER.warn("Nothing for " + event + " ... ");
				}
			}
		}
	}
}
