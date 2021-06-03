package com.hurui.atomix.cluster.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.atomix.cluster.BootstrapService;
import io.atomix.cluster.Node;
import io.atomix.cluster.NodeConfig;
import io.atomix.cluster.discovery.NodeDiscoveryConfig;
import io.atomix.cluster.discovery.NodeDiscoveryEvent;
import io.atomix.cluster.discovery.NodeDiscoveryEventListener;
import io.atomix.cluster.discovery.NodeDiscoveryProvider;
import io.atomix.utils.event.AbstractListenerManager;
import io.atomix.utils.net.Address;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Cluster membership provider that bootstraps membership from a pre-defined set of peers.
 * <p>
 * The bootstrap member provider takes a set of peer {@link BootstrapDiscoveryConfig#setNodes(Collection) addresses} and uses them
 * to join the cluster. Using the {@link io.atomix.cluster.messaging.MessagingService}, each node sends a heartbeat to
 * its configured bootstrap peers. Peers respond to each heartbeat message with a list of all known peers, thus
 * propagating membership information using a gossip style protocol.
 * <p>
 * A phi accrual failure detector is used to detect failures and remove peers from the configuration. In order to avoid
 * flapping of membership following a {@link io.atomix.cluster.ClusterMembershipEvent.Type#MEMBER_ADDED} event, the implementation attempts
 * to heartbeat all newly discovered peers before triggering a {@link io.atomix.cluster.ClusterMembershipEvent.Type#MEMBER_REMOVED} event.
 */
public class JdbcDiscoveryProvider extends AbstractListenerManager<NodeDiscoveryEvent, NodeDiscoveryEventListener> implements NodeDiscoveryProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDiscoveryProvider.class);
	private final Set<Node> nodes;
	private final JdbcDiscoveryConfig jdbcDiscoveryConfig;
	private static int DEFAULT_PORT = 5671;
	private static final int PORT_INCREMENT_RETRY = 20;

	public static final Type TYPE = new Type();
	private static volatile Configuration HIBERNATE_CONFIGURATION = null;
	public static volatile Address ADDRESS = null;
	
	public static Address getCurrentNodeAddress() throws UnknownHostException {
		if(ADDRESS == null) {
			synchronized(JdbcDiscoveryProvider.class) {
				if(ADDRESS == null) {
					for (int i = 0; i < PORT_INCREMENT_RETRY; i++) {
						if(isPortAvailable(DEFAULT_PORT)) {
							break;
						} else {
							DEFAULT_PORT ++;
						}
					}					
					ADDRESS = new Address(InetAddress.getLocalHost().getHostAddress(), DEFAULT_PORT);
				}
			}
		}
		return ADDRESS;
	}
	
	public static Configuration setHibernateConfiguration(Configuration configuration) {
		if(HIBERNATE_CONFIGURATION  == null) {
			synchronized(JdbcDiscoveryProvider.class) {
				if(HIBERNATE_CONFIGURATION  == null) {
					HIBERNATE_CONFIGURATION = configuration;
				}
			}
		}
		return HIBERNATE_CONFIGURATION; 
	}
	
	public static JdbcDiscoveryBuilder builder() {
		return new JdbcDiscoveryBuilder();
	}
	
	public static class Type implements NodeDiscoveryProvider.Type<JdbcDiscoveryConfig> {

		private static final String NAME = "jdbc";
		
		@Override
		public JdbcDiscoveryConfig newConfig() {
			return new JdbcDiscoveryConfig();
		}

		@Override
		public String name() {
			return NAME;
		}

		@Override
		public NodeDiscoveryProvider newProvider(JdbcDiscoveryConfig jdbcDiscoveryConfig) {
			return new JdbcDiscoveryProvider(jdbcDiscoveryConfig);
		}
		
	}
	
	public JdbcDiscoveryProvider(JdbcDiscoveryConfig jdbcDiscoveryConfig) {
		this.jdbcDiscoveryConfig = jdbcDiscoveryConfig;
		this.nodes = new HashSet<>();
		checkNotNull(HIBERNATE_CONFIGURATION ); // make sure that hibernate is configured
		LOGGER.info("Validated hibernate settings successfully.");
		JdbcDiscoveryHibernateUtil.setSessionFactory(HIBERNATE_CONFIGURATION);
		//this.nodes = Set.copyOf(jdbcDiscoveryConfig.getNodes().stream().map(Node::new).collect(Collectors.toList()));
	}
	
	@Override
	public NodeDiscoveryConfig config() {
		return jdbcDiscoveryConfig;
	}

	@Override
	public Set<Node> getNodes() {
		//Should probably get ir from JdbcDiscoveryConfig.class or make a jdbc call here to retrieve all the nodes from a db table		
		try {
			LOGGER.info("Getting All NODES");
			NodeConfig nodeConfig = new NodeConfig();
			String host = JdbcDiscoveryProvider.getCurrentNodeAddress().address().getHostAddress();
			Integer port = JdbcDiscoveryProvider.getCurrentNodeAddress().port();
			nodeConfig.setHost(host);
			nodeConfig.setPort(port);
			//save or update current node to database table
			ClusterDao clusterDao = new ClusterDao();
			clusterDao.saveOrUpdateCluster(Cluster.builder()
					.host(host)
					.port(port)
					.build());
			//retrieve all nodes from database table
			for(Cluster clusterMember: clusterDao.getCluster()) {
				NodeConfig cfg = new NodeConfig();
				cfg.setHost(clusterMember.getHost());
				cfg.setPort(clusterMember.getPort());
				Node node = new Node(cfg);
				nodes.add(node);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodes;
	}

	@Override
	public CompletableFuture<Void> join(BootstrapService bootstrap, Node localNode) {
		LOGGER.info("joined");
		//resolve node by periodically polling database and deleting dead nodes
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> leave(Node localNode) {
		LOGGER.info("left");
		post(null);
		return CompletableFuture.completedFuture(null);
	}

	private static boolean isPortAvailable(int port) {
		try(ServerSocket ignored = new ServerSocket(port)) {
			return Boolean.TRUE;
		} catch (IOException ex) {
			return Boolean.FALSE;
		}
	}
}
