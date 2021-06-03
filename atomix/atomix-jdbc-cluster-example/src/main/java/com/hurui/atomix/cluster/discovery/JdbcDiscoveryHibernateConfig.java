package com.hurui.atomix.cluster.discovery;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class JdbcDiscoveryHibernateConfig {

	private String driver;
	private String url;
	private String user;
	private String pass;
	private String dialect;
	private String showSql;
	private String currentSessionContextClass;
	private String hbm2dllAuto;
	
	public JdbcDiscoveryHibernateConfig() {
		
	}

	public JdbcDiscoveryHibernateConfig(String driver, String url, String user, String pass, String dialect,
			String showSql, String currentSessionContextClass, String hbm2dllAuto) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.dialect = dialect;
		this.showSql = showSql;
		this.currentSessionContextClass = currentSessionContextClass;
		this.hbm2dllAuto = hbm2dllAuto;
	}
	
	public String getDriver() {
		return driver;
	}
	
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getDialect() {
		return dialect;
	}
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	public String getShowSql() {
		return showSql;
	}
	
	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}
	
	public String getCurrentSessionContextClass() {
		return currentSessionContextClass;
	}
	
	public void setCurrentSessionContextClass(String currentSessionContextClass) {
		this.currentSessionContextClass = currentSessionContextClass;
	}
	
	public String getHbm2dllAuto() {
		return hbm2dllAuto;
	}
	
	public void setHbm2dllAuto(String hbm2dllAuto) {
		this.hbm2dllAuto = hbm2dllAuto;
	}
	
	public static JdbcDiscoveryHibernateConfigBuilder builder() {
		return new JdbcDiscoveryHibernateConfigBuilder();
	}
	
	public static class JdbcDiscoveryHibernateConfigBuilder {
		
		private String driver;
		private String url;
		private String user;
		private String pass;
		private String dialect;
		private String showSql;
		private String currentSessionContextClass;
		private String hbm2dllAuto;
		
		public JdbcDiscoveryHibernateConfigBuilder() {
			
		}
		
		public JdbcDiscoveryHibernateConfigBuilder driver(String driver) {
			this.driver = driver;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder user(String user) {
			this.user = user;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder pass(String pass) {
			this.pass = pass;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder dialect(String dialect) {
			this.dialect = dialect;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder showSql(String showSql) {
			this.showSql = showSql;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder currentSessionContextClass(String currentSessionContextClass) {
			this.currentSessionContextClass = currentSessionContextClass;
			return this;
		}
		
		public JdbcDiscoveryHibernateConfigBuilder hbm2dllAuto(String hbm2dllAuto) {
			this.hbm2dllAuto = hbm2dllAuto;
			return this;
		}
		
		public Configuration build() {
			Configuration configuration = new Configuration();
			Properties properties = new Properties();
			configuration.setProperties(properties);
			//Below are the mandatory parameters
			properties.put(Environment.URL, checkNotNull(this.url));
			properties.put(Environment.USER, checkNotNull(this.user));
			properties.put(Environment.PASS, checkNotNull(this.pass));
			
			//Below are the optional parameters with a default value provided by hibernate
			if(driver != null) {
				properties.put(Environment.DRIVER, this.driver);
			}
			
			if(dialect != null) {
				properties.put(Environment.DIALECT, this.dialect);
			}
			
			if(showSql != null) {
				properties.put(Environment.SHOW_SQL, showSql);
			}
			
			if(currentSessionContextClass != null) {
				properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, currentSessionContextClass);
			}
			
			if(hbm2dllAuto != null) {
				properties.put(Environment.HBM2DDL_AUTO, hbm2dllAuto);
			}
			
			configuration.addAnnotatedClass(Cluster.class);
			return configuration;
		}
	}
}
