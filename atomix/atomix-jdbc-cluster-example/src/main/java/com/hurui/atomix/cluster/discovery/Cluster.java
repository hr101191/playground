package com.hurui.atomix.cluster.discovery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(IpAddress.class)
@Table(name = "cluster")
public class Cluster implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4560728768721841614L;

	@Id
	@Column(name = "host")
	private String host;
	
	@Id
	@Column(name = "port")
	private Integer port;
	
	public Cluster() {

	}

	public Cluster(String host, Integer port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	public static ClusterBuilder builder() {
		return new ClusterBuilder();
	} 
	
	public static class ClusterBuilder {
		private String host;
		private Integer port;
		
		public ClusterBuilder() {

		}
		
		public ClusterBuilder host(String host) {
			this.host = host;
			return this;
		}

		public ClusterBuilder port(Integer port) {
			this.port = port;
			return this;
		}
		
		public Cluster build() {
			Cluster cluster = new Cluster();
			cluster.setHost(this.host);
			cluster.setPort(this.port);
			return cluster;
		} 
	}
	
}
