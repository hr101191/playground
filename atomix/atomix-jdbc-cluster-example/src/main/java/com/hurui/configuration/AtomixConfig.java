package com.hurui.configuration;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hurui.atomix.cluster.discovery.JdbcDiscoveryHibernateConfig;
import com.hurui.atomix.cluster.discovery.JdbcDiscoveryProvider;

import io.atomix.cluster.Node;
import io.atomix.cluster.NodeConfig;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.protocols.backup.partition.PrimaryBackupPartitionGroup;

@Configuration
public class AtomixConfig {
	
	@Bean
	public Atomix atomix() throws UnknownHostException {
		JdbcDiscoveryProvider.setHibernateConfiguration(JdbcDiscoveryHibernateConfig.builder()
				.driver("org.mariadb.jdbc.Driver")
				.url("jdbc:mariadb://localhost:3306/atomix")
				.user("root")
				.pass("")
				.hbm2dllAuto("update")
				.build());
		NodeConfig nodeConfig = new NodeConfig();
		NodeConfig nodeConfig1 = new NodeConfig();
		try {
			nodeConfig.setId(JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress() + ":" + 5671);
			nodeConfig.setHost(JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress());
			nodeConfig.setPort(5671);
			//temp setting to emulate node 2
			nodeConfig1.setHost(JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress());
			nodeConfig1.setPort(5672);
			nodeConfig.setId(JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress() + ":" + 5672);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Node node = new Node(nodeConfig);
		Node node1 = new Node(nodeConfig1);
		Atomix atomix = Atomix.builder()
				//.withPort(9999)
				.withMemberId(JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress() + ":" + JdbcDiscoveryProvider.getCurrentNodeAddress().port())
				//.withMulticastEnabled()
				//.withMulticastEnabled()
				.withManagementGroup(PrimaryBackupPartitionGroup.builder("raft")
					    .build())
				.withPartitionGroups(PrimaryBackupPartitionGroup.builder("raft")
					    .build())
				.withAddress(JdbcDiscoveryProvider.getCurrentNodeAddress())
				//.withMembershipProvider(BootstrapDiscoveryProvider.builder().withNodes(node, node1).build())
				.withMembershipProvider(JdbcDiscoveryProvider.builder().build())
				.build();
		atomix.start().join();
		return atomix;
	} 
}
