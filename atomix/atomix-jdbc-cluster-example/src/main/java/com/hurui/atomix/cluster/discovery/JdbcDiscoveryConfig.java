package com.hurui.atomix.cluster.discovery;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import io.atomix.cluster.NodeConfig;
import io.atomix.cluster.discovery.NodeDiscoveryConfig;

import static com.google.common.base.Preconditions.checkNotNull;

public class JdbcDiscoveryConfig extends NodeDiscoveryConfig {
	private static final int DEFAULT_HEARTBEAT_INTERVAL = 1000;
	private static final int DEFAULT_FAILURE_TIMEOUT = 10000;
	private static final int DEFAULT_PHI_FAILURE_THRESHOLD = 10;

	private Duration heartbeatInterval = Duration.ofMillis(DEFAULT_HEARTBEAT_INTERVAL);
	private int failureThreshold = DEFAULT_PHI_FAILURE_THRESHOLD;
	private Duration failureTimeout = Duration.ofMillis(DEFAULT_FAILURE_TIMEOUT);	
	private Collection<NodeConfig> nodes = Collections.emptySet();
	private Properties hibernateProperties;
	
	@Override
	public JdbcDiscoveryProvider.Type getType() {
		return JdbcDiscoveryProvider.TYPE;
	}
	
	/**
	 * Returns the configured bootstrap nodes.
	 *
	 * @return the configured bootstrap nodes
	 */
	public Collection<NodeConfig> getNodes() {
		NodeConfig nodeConfig = new NodeConfig();
		try {
			nodeConfig.setHost(JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress());
			nodeConfig.setPort(JdbcDiscoveryProvider.getCurrentNodeAddress().port());
			//Get all the nodeConfig from the database
			//nodes.add(nodeConfig);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	/**
	 * Returns the heartbeat interval.
	 *
	 * @return the heartbeat interval
	 */
	@Deprecated
	public Duration getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/**
	 * Sets the heartbeat interval.
	 *
	 * @param heartbeatInterval the heartbeat interval
	 * @return the group membership configuration
	 */
	@Deprecated
	public JdbcDiscoveryConfig setHeartbeatInterval(Duration heartbeatInterval) {
		this.heartbeatInterval = checkNotNull(heartbeatInterval);
		return this;
	}

	/**
	 * Returns the failure detector threshold.
	 *
	 * @return the failure detector threshold
	 */
	@Deprecated
	public int getFailureThreshold() {
		return failureThreshold;
	}

	/**
	 * Sets the failure detector threshold.
	 *
	 * @param failureThreshold the failure detector threshold
	 * @return the group membership configuration
	 */
	@Deprecated
	public JdbcDiscoveryConfig setFailureThreshold(int failureThreshold) {
		this.failureThreshold = failureThreshold;
		return this;
	}

	/**
	 * Returns the base failure timeout.
	 *
	 * @return the base failure timeout
	 */
	@Deprecated
	public Duration getFailureTimeout() {
		return failureTimeout;
	}

	/**
	 * Sets the base failure timeout.
	 *
	 * @param failureTimeout the base failure timeout
	 * @return the group membership configuration
	 */
	@Deprecated
	public JdbcDiscoveryConfig setFailureTimeout(Duration failureTimeout) {
		this.failureTimeout = checkNotNull(failureTimeout);
		return this;
	}

	public Properties getHibernateProperties() {
		return hibernateProperties;
	}

	public void setHibernateProperties(Properties hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}
}
