package com.hurui.atomixjdbcclusterexample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import com.hurui.atomix.cluster.discovery.JdbcDiscoveryProvider;

import io.atomix.core.Atomix;

import static io.atomix.utils.concurrent.Threads.namedThreads;

@ComponentScan("com.hurui")
@SpringBootApplication
public class AtomixJdbcClusterExampleApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AtomixJdbcClusterExampleApplication.class);

	@Autowired
	private Atomix atomix;
	
	private final ScheduledExecutorService broadcastScheduler = Executors.newSingleThreadScheduledExecutor(namedThreads("atomix-cluster-broadcast", LOGGER));
	
	public static void main(String[] args) {
		SpringApplication.run(AtomixJdbcClusterExampleApplication.class, args);
	}
	
	@EventListener
	public void test(ApplicationStartedEvent event) {
		broadcastScheduler.scheduleAtFixedRate(() -> {
			LOGGER.info("clustermember check");
			atomix.getMembershipService().getMembers().forEach(member -> {
				LOGGER.info("clustermember: {}", member);
			});
		}, 10000, 100, TimeUnit.MILLISECONDS);
		
	}

}
