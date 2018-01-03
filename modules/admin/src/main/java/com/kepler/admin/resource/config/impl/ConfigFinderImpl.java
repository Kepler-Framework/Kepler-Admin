package com.kepler.admin.resource.config.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;

import com.kepler.ack.impl.AckFuture;
import com.kepler.ack.impl.AckTimeOutImpl;
import com.kepler.admin.resource.config.Config;
import com.kepler.admin.resource.config.ConfigFinder;
import com.kepler.admin.trace.impl.TraceTask;
import com.kepler.connection.reject.AddressReject;
import com.kepler.connection.reject.DefaultRejectContext;
import com.kepler.connection.reject.ServiceReject;
import com.kepler.host.Host;
import com.kepler.host.impl.DefaultHostContext;
import com.kepler.id.impl.DefaultIDGenerators;
import com.kepler.invoker.forkjoin.impl.ForkJoinInvoker;
import com.kepler.invoker.impl.BroadcastInvoker;
import com.kepler.invoker.impl.CompeteInvoker;
import com.kepler.invoker.impl.DemoteInvoker;
import com.kepler.invoker.impl.MainInvoker;
import com.kepler.mock.impl.DefaultMockerContext;
import com.kepler.serial.SerialID;
import com.kepler.serial.Serials;
import com.kepler.thread.ThreadFactoryConfig;
import com.kepler.token.impl.AccessTokenContext;
import com.kepler.trace.Trace;
import com.kepler.zookeeper.ZkContext;
import com.kepler.zookeeper.ZkFactoryConfig;

/**
 * @author kim 2015年12月26日
 */
public class ConfigFinderImpl implements ConfigFinder {

	private static final Map<String, Object> DATA = new HashMap<String, Object>();

	private static final Log LOGGER = LogFactory.getLog(ConfigFinderImpl.class);

	/**
	 * Key集合
	 */
	private final List<String> keys = new ArrayList<String>();

	private final CuratorFramework client;

	private final Serials serials;

	public ConfigFinderImpl(CuratorFramework client, Serials serials) {
		this.serials = serials;
		this.client = client;
		this.init();
	}

	private void init() {
		this.keys.add(AccessTokenContext.TOKEN_PROFILE_KEY);
		this.keys.add(MainInvoker.THRESHOLD_ENABLED_KEY);
		this.keys.add(DefaultIDGenerators.GENERATOR_KEY);
		this.keys.add(ThreadFactoryConfig.KEEPALIVE_KEY);
		this.keys.add(ThreadFactoryConfig.CORE_KEY);
		this.keys.add(ThreadFactoryConfig.MAX_KEY);
		this.keys.add(DefaultRejectContext.REJECT_KEY);
		this.keys.add(DefaultHostContext.ROUTING_KEY);
		this.keys.add(DefaultMockerContext.MOCK_KEY);
		this.keys.add(AckTimeOutImpl.DEMOTION_KEY);
		this.keys.add(BroadcastInvoker.CANCEL_KEY);
		this.keys.add(MainInvoker.THRESHOLD_KEY);
		this.keys.add(AddressReject.ADDRESS_KEY);
		this.keys.add(CompeteInvoker.CANCEL_KEY);
		this.keys.add(ZkFactoryConfig.RESET_KEY);
		this.keys.add(ForkJoinInvoker.TAGS_KEY);
		this.keys.add(ServiceReject.REJECT_KEY);
		this.keys.add(DemoteInvoker.DEMOTE_KEY);
		this.keys.add(AckFuture.TIMEOUT_KEY);
		this.keys.add(TraceTask.ENABLED_KEY);
		this.keys.add(SerialID.SERIAL_KEY);
		this.keys.add(Trace.ENABLED_KEY);
		this.keys.add(Host.TAG_KEY);
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Config sid(String sid) {
		// 组合路径
		String path = ZkContext.ROOT + ZkContext.CONFIG + "/" + sid;
		try {
			return new ConfigImpl(path, this.serials.def4input().input(this.client.getData().forPath(path), Map.class));
		} catch (Exception e) {
			ConfigFinderImpl.LOGGER.error(e.getMessage(), e);
			return new ConfigImpl(path, ConfigFinderImpl.DATA);
		}
	}

	public Collection<String> keys() {
		return this.keys;
	}
}
