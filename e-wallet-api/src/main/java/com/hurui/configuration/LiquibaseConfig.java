package com.hurui.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import liquibase.integration.spring.SpringLiquibase;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "application.spring.liquibase")
@Data
@RequiredArgsConstructor
public class LiquibaseConfig {
	
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private String changeLogPath;

	@Bean
	public SpringLiquibase springLiquibase() {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(driverClassName);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(password);
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setDataSource(dataSourceBuilder.build());
		springLiquibase.setChangeLog(changeLogPath);
		return springLiquibase;
	}
	
}
