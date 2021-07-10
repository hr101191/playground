package com.hurui.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ignite.cache.hibernate.HibernateRegionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.query.criteria.LiteralHandlingMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hurui.model.ignite.Person;

@DependsOn("ignite") //Ignite hibernate l2 cache config can only be loaded after ignite instance is running
@Configuration
@EnableTransactionManagement
public class HibernateL2CacheConfig {

	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put(Environment.USE_QUERY_CACHE, "true");
		hibernateProperties.put(Environment.USE_SECOND_LEVEL_CACHE, "true");
		hibernateProperties.put(Environment.GENERATE_STATISTICS, "true");
		hibernateProperties.put(Environment.CRITERIA_LITERAL_HANDLING_MODE, LiteralHandlingMode.BIND);
		hibernateProperties.put(Environment.CACHE_REGION_FACTORY, "org.apache.ignite.cache.hibernate.HibernateRegionFactory");
		hibernateProperties.put("org.apache.ignite.hibernate.default_access_type", "READ_WRITE");
		hibernateProperties.put("org.apache.ignite.hibernate.ignite_instance_name", "test");
		hibernateProperties.put(Environment.HBM2DDL_AUTO, "create");
		hibernateProperties.put(Environment.SHOW_SQL, "true");
		return hibernateProperties;
	}
	
	@Bean
	public LocalSessionFactoryBean localSessionFactoryBean(DataSource dataSource, Properties hibernateProperties) {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		localSessionFactoryBean.setPackagesToScan("com.hurui.model.ignite");
		localSessionFactoryBean.setCacheRegionFactory(new HibernateRegionFactory());
		localSessionFactoryBean.setAnnotatedPackages("com.hurui.model.ignite");
		localSessionFactoryBean.setAnnotatedClasses(Person.class);
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);	
		return localSessionFactoryBean;
	}
	
	@Bean
	public PlatformTransactionManager platformTransactionManager(LocalSessionFactoryBean localSessionFactoryBean) {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(localSessionFactoryBean.getObject());
		return hibernateTransactionManager;
	}
}
