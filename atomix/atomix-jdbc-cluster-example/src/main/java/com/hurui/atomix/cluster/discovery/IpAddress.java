package com.hurui.atomix.cluster.discovery;

import java.io.Serializable;

public class IpAddress implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5297160512245277421L;
	
	private String host;
	private Integer port;
	
	public IpAddress() {

	}
	
	public IpAddress(String host, Integer port) {
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
	
	public IpAddress host(String host) {
		this.host = host;
		return this;
	}
	
	public IpAddress port(Integer port) {
		this.port = port;
		return this;
	}
	
	public static IpAddressBuilder builder() {
		return new IpAddressBuilder();
	} 
	
	public static class IpAddressBuilder {
		private String host;
		private Integer port;
		
		public IpAddressBuilder() {

		}
		
		public IpAddressBuilder host(String host) {
			this.host = host;
			return this;
		}

		public IpAddressBuilder port(Integer port) {
			this.port = port;
			return this;
		}
		
		public IpAddress build() {
			IpAddress ipAddress = new IpAddress();
			ipAddress.setHost(this.host);
			ipAddress.setPort(this.port);
			return ipAddress;
		} 
	}
	
}
