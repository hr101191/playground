package com.hurui.configuration;

import java.util.UUID;

import javax.sql.DataSource;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.jdbc.TcpDiscoveryJdbcIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hurui.model.ignite.Person;

@Configuration
public class IgniteConfig {
	
	@Bean
	public IgniteConfiguration igniteConfiguration(DataSource dataSource, CacheConfiguration<?, ?> personHibernateCacheConfiguration, CacheConfiguration<?, ?> standardQueryCacheConfiguration, CacheConfiguration<?, ?> updateTimestampsCacheConfiguration) {
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
		igniteConfiguration.setIgniteInstanceName("test");
		igniteConfiguration.setPeerClassLoadingEnabled(Boolean.TRUE);
		
		TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
		TcpDiscoveryJdbcIpFinder tcpDiscoveryJdbcIpFinder = new TcpDiscoveryJdbcIpFinder();
		tcpDiscoveryJdbcIpFinder.setDataSource(dataSource);
		tcpDiscoveryJdbcIpFinder.setShared(Boolean.TRUE);
		tcpDiscoverySpi.setIpFinder(tcpDiscoveryJdbcIpFinder);
		
		DataStorageConfiguration storageCfg = new DataStorageConfiguration();
		storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
				
		igniteConfiguration.setDataStorageConfiguration(storageCfg);
		igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
		igniteConfiguration.setCacheConfiguration(personHibernateCacheConfiguration, standardQueryCacheConfiguration, updateTimestampsCacheConfiguration);
		return igniteConfiguration;
	}
	
	@SuppressWarnings("deprecation")
	@Bean(destroyMethod = "close")
	public Ignite ignite(IgniteConfiguration igniteConfiguration) {
		Ignite ignite = Ignition.start(igniteConfiguration);	
		ignite.active(Boolean.TRUE);
		return ignite;
	}
	
	@Bean
	public CacheConfiguration<?, ?> personHibernateCacheConfiguration() {
		CacheConfiguration<?, ?> cacheConfiguration = new CacheConfiguration<UUID, Person>(Person.class.getName());
		cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
		cacheConfiguration.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
		return cacheConfiguration;
	}
	
	@Bean
	public CacheConfiguration<?, ?> standardQueryCacheConfiguration() {
		CacheConfiguration<?, ?> cacheConfiguration = new CacheConfiguration<>("default-query-results-region");
		cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
		cacheConfiguration.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
		return cacheConfiguration;
	}
	
	@Bean
	public CacheConfiguration<Object, Object> updateTimestampsCacheConfiguration() {
		CacheConfiguration<Object, Object> cacheConfiguration = new CacheConfiguration<>("default-update-timestamps-region");
		cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
		cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
		cacheConfiguration.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
		return cacheConfiguration;
	}

}
