package com.example.qpidjms.autoconfigure;

import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jms.autoconfigure.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ConnectionFactory.class)
class QpidJmsConnectionFactoryConfiguration {

	@Bean
	@ConditionalOnBooleanProperty(name = "spring.jms.cache.enabled", havingValue = false)
	JmsConnectionFactory jmsConnectionFactory(QpidJmsConnectionDetails connectionDetails) {
		return createJmsConnectionFactory(connectionDetails);
	}

	private static JmsConnectionFactory createJmsConnectionFactory(QpidJmsConnectionDetails connectionDetails) {
		return new JmsConnectionFactory(connectionDetails.getUsername(), connectionDetails.getPassword(),
				connectionDetails.getBrokerUrl());
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(CachingConnectionFactory.class)
	@ConditionalOnBooleanProperty(name = "spring.jms.cache.enabled", matchIfMissing = true)
	static class CachingConnectionFactoryConfiguration {

		@Bean
		CachingConnectionFactory jmsConnectionFactory(JmsProperties jmsProperties,
				QpidJmsConnectionDetails connectionDetails) {
			JmsProperties.Cache cacheProperties = jmsProperties.getCache();
			CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
					createJmsConnectionFactory(connectionDetails));
			connectionFactory.setCacheConsumers(cacheProperties.isConsumers());
			connectionFactory.setCacheProducers(cacheProperties.isProducers());
			connectionFactory.setSessionCacheSize(cacheProperties.getSessionCacheSize());
			return connectionFactory;
		}

	}

}
