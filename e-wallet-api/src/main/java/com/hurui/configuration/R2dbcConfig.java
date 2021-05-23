package com.hurui.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;

import io.r2dbc.pool.PoolingConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableR2dbcRepositories("com.hurui.repository")
@RequiredArgsConstructor
public class R2dbcConfig {
	
	@Bean
	public SpringLiquibase springLiquibase() {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.h2.Driver");
		dataSourceBuilder.url("jdbc:h2:mem:e-wallet");
		dataSourceBuilder.username("sa");
		dataSourceBuilder.password("password");
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setDataSource(dataSourceBuilder.build());
		springLiquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.xml");
		return springLiquibase;
	}
	
	//Example showing bootstrapping of r2dbc ConnectionFactory to allow performance fine-tuning
	@Bean
	@DependsOn("springLiquibase")
	public DatabaseClient databaseClient() {
		PoolingConnectionFactoryProvider provider = new PoolingConnectionFactoryProvider();
		ConnectionFactory connectionFactory = provider.create(ConnectionFactoryOptions.builder()
//				.option(ConnectionFactoryOptions.HOST, "localhost") //not used for h2 database
//				.option(ConnectionFactoryOptions.PORT, 6603) //not used for h2 database
				.option(ConnectionFactoryOptions.DATABASE, "e-wallet")
				.option(ConnectionFactoryOptions.DRIVER, PoolingConnectionFactoryProvider.POOLING_DRIVER)
				.option(ConnectionFactoryOptions.PROTOCOL, "h2:mem")
				.option(ConnectionFactoryOptions.USER, "sa")
				.option(ConnectionFactoryOptions.PASSWORD, "password")
				.option(PoolingConnectionFactoryProvider.INITIAL_SIZE, 10) //Sample tuning for connection pool.
				.option(PoolingConnectionFactoryProvider.MAX_SIZE, 30)
				.build());
		return DatabaseClient.builder()
				.connectionFactory(connectionFactory)
				.build();	
	}
	
	@Bean
	public ReactiveTransactionManager reactiveTransactionManager(DatabaseClient databaseClient) {
		return new R2dbcTransactionManager(databaseClient.getConnectionFactory());
	}
}
