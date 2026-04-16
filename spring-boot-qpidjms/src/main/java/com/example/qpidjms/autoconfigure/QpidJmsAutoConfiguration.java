package com.example.qpidjms.autoconfigure;

import java.net.URI;

import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.boot.jms.autoconfigure.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration(before = JmsAutoConfiguration.class)
@ConditionalOnClass({ ConnectionFactory.class, JmsConnectionFactory.class })
@ConditionalOnMissingBean(ConnectionFactory.class)
@ConditionalOnProperty("spring.qpidjms.broker-url")
@EnableConfigurationProperties({ QpidJmsProperties.class, JmsProperties.class })
@Import(QpidJmsConnectionFactoryConfiguration.class)
public final class QpidJmsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	QpidJmsConnectionDetails qpidJmsConnectionDetails(QpidJmsProperties properties) {
		return new PropertiesQpidJmsConnectionDetails(properties);
	}

	static class PropertiesQpidJmsConnectionDetails implements QpidJmsConnectionDetails {

		private final QpidJmsProperties properties;

		PropertiesQpidJmsConnectionDetails(QpidJmsProperties properties) {
			this.properties = properties;
		}

		@Override
		public @Nullable URI getBrokerUrl() {
			return this.properties.getBrokerUrl();
		}

		@Override
		public @Nullable String getUsername() {
			return this.properties.getUsername();
		}

		@Override
		public @Nullable String getPassword() {
			return this.properties.getPassword();
		}

	}

}
