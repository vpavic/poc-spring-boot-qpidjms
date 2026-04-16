package com.example.qpidjms.autoconfigure;

import java.net.URI;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.jms.autoconfigure.JmsPoolConnectionFactoryProperties;

@ConfigurationProperties("spring.qpidjms")
public class QpidJmsProperties {

	private @Nullable URI brokerUrl;

	private @Nullable String username;

	private @Nullable String password;

	@NestedConfigurationProperty
	private final JmsPoolConnectionFactoryProperties pool = new JmsPoolConnectionFactoryProperties();

	public @Nullable URI getBrokerUrl() {
		return this.brokerUrl;
	}

	public void setBrokerUrl(URI brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public @Nullable String getUsername() {
		return this.username;
	}

	public void setUsername(@Nullable String username) {
		this.username = username;
	}

	public @Nullable String getPassword() {
		return this.password;
	}

	public void setPassword(@Nullable String password) {
		this.password = password;
	}

	public JmsPoolConnectionFactoryProperties getPool() {
		return this.pool;
	}

}
