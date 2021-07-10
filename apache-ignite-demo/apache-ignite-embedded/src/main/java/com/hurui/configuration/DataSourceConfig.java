package com.hurui.configuration;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.driverClassName("org.mariadb.jdbc.Driver")
				.url("jdbc:mariadb://localhost:3306/ignite")
				.username("root")
				.password("")
				.build();
	}
	
}
