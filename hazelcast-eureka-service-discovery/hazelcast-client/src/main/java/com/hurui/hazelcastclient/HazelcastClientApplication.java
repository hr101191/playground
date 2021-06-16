package com.hurui.hazelcastclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.hazelcast.jet.contrib.autoconfigure.HazelcastJetAutoConfiguration;

@ComponentScan("com.hurui")
@SpringBootApplication(exclude = {HazelcastAutoConfiguration.class, HazelcastJetAutoConfiguration.class, org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
@EnableDiscoveryClient
public class HazelcastClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HazelcastClientApplication.class, args);
	}

}
