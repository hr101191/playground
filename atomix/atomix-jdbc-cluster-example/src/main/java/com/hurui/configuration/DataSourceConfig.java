package com.hurui.configuration;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DataSourceConfig {
	
	@Bean
	public Server server() throws SQLException {
		Server server = Server.createTcpServer("-tcpPort", "9123", "-tcpAllowOthers").start();
		return server;
	}
	
	@Bean
	public Server WebServer() throws SQLException {
		Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9000").start();
		return webServer;
	}

}
