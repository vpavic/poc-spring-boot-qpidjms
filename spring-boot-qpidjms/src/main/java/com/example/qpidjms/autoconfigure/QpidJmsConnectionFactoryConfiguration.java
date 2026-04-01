package com.example.qpidjms.autoconfigure;

import jakarta.jms.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jms.autoconfigure.JmsPoolConnectionFactoryFactory;
import org.springframework.boot.jms.autoconfigure.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ConnectionFactory.class)
class QpidJmsConnectionFactoryConfiguration {

	private static JmsConnectionFactory createJmsConnectionFactory(QpidJmsConnectionDetails connectionDetails) {
		return new JmsConnectionFactory(connectionDetails.getUsername(), connectionDetails.getPassword(),
				connectionDetails.getBrokerUrl());
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBooleanProperty(name = "spring.qpidjms.pool.enabled", havingValue = false, matchIfMissing = true)
	static class SimpleConnectionFactoryConfiguration {

		@Bean
		@ConditionalOnBooleanProperty(name = "spring.jms.cache.enabled", havingValue = false)
		JmsConnectionFactory jmsConnectionFactory(QpidJmsConnectionDetails connectionDetails) {
			return createJmsConnectionFactory(connectionDetails);
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

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ JmsPoolConnectionFactory.class, PooledObject.class })
	@ConditionalOnBooleanProperty("spring.qpidjms.pool.enabled")
	static class PooledConnectionFactoryConfiguration {

		@Bean(destroyMethod = "stop")
		JmsPoolConnectionFactory jmsConnectionFactory(QpidJmsProperties properties,
				QpidJmsConnectionDetails connectionDetails) {
			JmsConnectionFactory connectionFactory = createJmsConnectionFactory(connectionDetails);
			return new JmsPoolConnectionFactoryFactory(properties.getPool())
					.createPooledConnectionFactory(connectionFactory);
		}

	}

}
