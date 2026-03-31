package com.example.qpidjms.autoconfigure;

import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.boot.jms.autoconfigure.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@AutoConfiguration(before = JmsAutoConfiguration.class)
@ConditionalOnClass({ ConnectionFactory.class, JmsConnectionFactory.class })
@ConditionalOnMissingBean(ConnectionFactory.class)
@EnableConfigurationProperties({ QpidJmsProperties.class, JmsProperties.class })
public final class QpidJmsAutoConfiguration {

	@Bean
	@ConditionalOnBooleanProperty(name = "spring.jms.cache.enabled", havingValue = false)
	JmsConnectionFactory jmsConnectionFactory(QpidJmsProperties properties) {
		return createJmsConnectionFactory(properties);
	}

	private static JmsConnectionFactory createJmsConnectionFactory(QpidJmsProperties properties) {
		return new JmsConnectionFactory(properties.getUsername(), properties.getPassword(), properties.getBrokerUrl());
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(CachingConnectionFactory.class)
	@ConditionalOnBooleanProperty(name = "spring.jms.cache.enabled", matchIfMissing = true)
	static class CachingConnectionFactoryConfiguration {

		@Bean
		CachingConnectionFactory jmsConnectionFactory(JmsProperties jmsProperties, QpidJmsProperties properties) {
			JmsProperties.Cache cacheProperties = jmsProperties.getCache();
			CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
					createJmsConnectionFactory(properties));
			connectionFactory.setCacheConsumers(cacheProperties.isConsumers());
			connectionFactory.setCacheProducers(cacheProperties.isProducers());
			connectionFactory.setSessionCacheSize(cacheProperties.getSessionCacheSize());
			return connectionFactory;
		}

	}

}
