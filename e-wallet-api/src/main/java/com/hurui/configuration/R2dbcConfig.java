package com.hurui.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;

import io.r2dbc.pool.PoolingConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "application.spring.r2dbc")
@EnableR2dbcRepositories("com.hurui.repository")
@EqualsAndHashCode(callSuper=false)
@Data
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {
	
	private String host;
	private Integer port;
	private String database;
	private String driver;
	private String protocol;
	private String user;
	private String password;
	private Integer initialSize;
	private Integer maxSize;

	//Example showing bootstrapping of r2dbc ConnectionFactory to allow performance fine-tuning
	@Bean
	@DependsOn("springLiquibase")
	@Override
	public ConnectionFactory connectionFactory() {
		PoolingConnectionFactoryProvider provider = new PoolingConnectionFactoryProvider();
		ConnectionFactory connectionFactory = provider.create(ConnectionFactoryOptions.builder()
				.option(ConnectionFactoryOptions.HOST, host) //not used by h2:mem database but no harm keeping it to maintain code consistency with switched to an actual rdbms
				.option(ConnectionFactoryOptions.PORT, port) //not used by h2:mem database but no harm keeping it to maintain code consistency with switched to an actual rdbms
				.option(ConnectionFactoryOptions.DATABASE, database)
				.option(ConnectionFactoryOptions.DRIVER, driver)
				.option(ConnectionFactoryOptions.PROTOCOL, protocol)
				.option(ConnectionFactoryOptions.USER, user)
				.option(ConnectionFactoryOptions.PASSWORD, password)
				.option(PoolingConnectionFactoryProvider.INITIAL_SIZE, initialSize) //Sample tuning for connection pool.
				.option(PoolingConnectionFactoryProvider.MAX_SIZE, maxSize)
				.build());
		return connectionFactory;
	}
	
	@Bean
	public ReactiveTransactionManager reactiveTransactionManager(DatabaseClient databaseClient) {
		return new R2dbcTransactionManager(databaseClient.getConnectionFactory());
	}

}
