package com.hurui.configuration;

import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;

@Configuration
public class HazelcastConfig {
	
	@Bean
	public ClientConfig clientConfig(@Value("${eureka.client.serviceUrl.defaultZone}") String defaultServiceUrl) throws UnknownHostException {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getNetworkConfig().getEurekaConfig()
			.setEnabled(Boolean.TRUE)
			.setProperty("use-classpath-eureka-client-props", "false") //Prevent looking up classpath for default property file. We want to use our custom config below.
			.setProperty("registration.enabled", "false") //Prevent hazelcast client from registering with eureka. 
			.setProperty("namespace", "hazelcast") //XML namespace, defaults to hazelcast. Not used			
			.setProperty("name", "hazelcast-cluster") //Eureka application name to lookup in order to detect the Hazelcast server cluster
			.setProperty("serviceUrl.default", defaultServiceUrl); //Eureka discovery uri, comma seperated
		return clientConfig;
	}
	
	@Bean(destroyMethod = "shutdown")
	public JetInstance jetInstance(ClientConfig clientConfig) {
		return Jet.newJetClient(clientConfig);
	}
		
	@Bean(destroyMethod = "shutdown")
	public HazelcastInstance hazelcastInstance(JetInstance jetInstance) {
		return jetInstance.getHazelcastInstance();
	}
}
