package com.hurui.atomix.cluster.discovery;

import java.time.Duration;

import io.atomix.cluster.discovery.NodeDiscoveryBuilder;
import io.atomix.cluster.discovery.NodeDiscoveryProvider;

public class JdbcDiscoveryBuilder extends NodeDiscoveryBuilder {

	private final JdbcDiscoveryConfig jdbcDiscoveryConfig = new JdbcDiscoveryConfig();
	
	/**
	* Sets the failure detection heartbeat interval.
	*
	* @param heartbeatInterval the failure detection heartbeat interval
	* @return the location provider builder
	*/
	public JdbcDiscoveryBuilder withHeartbeatInterval(Duration heartbeatInterval) {
		jdbcDiscoveryConfig.setHeartbeatInterval(heartbeatInterval);
		return this;
	}

	/**
	* Sets the phi accrual failure threshold.
	*
	* @param failureThreshold the phi accrual failure threshold
	* @return the location provider builder
	*/
	public JdbcDiscoveryBuilder withFailureThreshold(int failureThreshold) {
		jdbcDiscoveryConfig.setFailureThreshold(failureThreshold);
		return this;
	}

	/**
	* Sets the failure timeout to use prior to phi failure detectors being populated.
	*
	* @param failureTimeout the failure timeout
	* @return the location provider builder
	*/
	public JdbcDiscoveryBuilder withFailureTimeout(Duration failureTimeout) {
		jdbcDiscoveryConfig.setFailureTimeout(failureTimeout);
		return this;
	}
	
	@Override
	public NodeDiscoveryProvider build() {
		return new JdbcDiscoveryProvider(jdbcDiscoveryConfig);
	}

}
