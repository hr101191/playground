package com.hurui.atomix.cluster.discovery;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class JdbcDiscoveryHibernateUtil {

	private Configuration configuration;
	
	private static volatile SessionFactory sessionFactory;
	
	public static SessionFactory getSessionFactory() throws Exception {
		if(sessionFactory == null) {
			throw new Exception("Hibernate Session Factory is not configured!");
		}
		return sessionFactory;
	}
	
	public static void setSessionFactory(Configuration configuration) {
		synchronized(JdbcDiscoveryHibernateUtil.class) {
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties())
					.build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
