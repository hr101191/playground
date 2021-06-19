package com.hurui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.RestApiConfig;
import com.hazelcast.config.RestEndpointGroup;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;

@Configuration
public class HazelcastConfig {

	@Bean
	public Config config(@Value("${eureka.client.serviceUrl.defaultZone}") String defaultServiceUrl) {
		System.setProperty("archaius.dynamicProperty.disableSystemConfig", "true");
		Config config = new Config();
		RestApiConfig restApiConfig = new RestApiConfig()
		        .setEnabled(true)
		        .enableGroups(RestEndpointGroup.DATA);
		config.getNetworkConfig().setRestApiConfig(restApiConfig);
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(Boolean.FALSE);
		config.getNetworkConfig().getJoin().getEurekaConfig()
			.setEnabled(Boolean.TRUE)
			.setProperty("self-registration", "true")
			.setProperty("use-classpath-eureka-client-props", "false") //Prevent looking up classpath for default property file. We want to use our custom config below.
			.setProperty("namespace", "hazelcast") //XML namespace, defaults to hazelcast. Not used			
			.setProperty("name", "hazelcast-cluster") //Hazelcast cluster name to be registered in Eureka
			.setProperty("serviceUrl.default", defaultServiceUrl); //Eureka discovery uri, comma seperated	
		return config;
	}
	
	@Bean(destroyMethod = "shutdown")
	public JetInstance jetInstance(Config config) {
		JetConfig jetConfig = new JetConfig();
		jetConfig.setHazelcastConfig(config);
		return Jet.newJetInstance(jetConfig);
	}
	
	@Bean(destroyMethod = "shutdown")
	public HazelcastInstance hazelcastInstance(JetInstance jetInstance) {
		return jetInstance.getHazelcastInstance();
	}

}
