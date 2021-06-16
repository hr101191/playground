package com.hurui.hazelcastservercluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.hazelcast.jet.contrib.autoconfigure.HazelcastJetAutoConfiguration;

@ComponentScan("com.hurui")
@SpringBootApplication(exclude = {HazelcastAutoConfiguration.class, HazelcastJetAutoConfiguration.class})
@EnableDiscoveryClient
public class HazelcastServerClusterApplication {

	public static void main(String[] args) {
		SpringApplication.run(HazelcastServerClusterApplication.class, args);
	}

}
