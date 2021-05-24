package com.hurui.ewalletapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.hurui.configuration.LiquibaseConfig;
import com.hurui.configuration.R2dbcConfig;

@ComponentScan("com.hurui")
@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class}) //we want to bootstrap the r2dbc config ourselves to allow fine tuning of its settings
@EnableConfigurationProperties(value = {LiquibaseConfig.class, R2dbcConfig.class})
public class EWalletApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWalletApiApplication.class, args);
	}

}
