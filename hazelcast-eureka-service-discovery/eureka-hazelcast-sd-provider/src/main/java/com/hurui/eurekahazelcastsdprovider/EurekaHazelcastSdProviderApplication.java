package com.hurui.eurekahazelcastsdprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaHazelcastSdProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaHazelcastSdProviderApplication.class, args);
	}

}
