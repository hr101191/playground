package com.hurui.configuration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiRouter {

	@Bean
	public OpenAPI eWalletOpenApi() {
		return new OpenAPI()
				.info(new Info().title("E-Wallet API").version("1.0"));
	}
	
	@Bean
	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi.builder()
				.group("e-wallet-api")
				.pathsToMatch("/api/**")
				.build();
	}
}
