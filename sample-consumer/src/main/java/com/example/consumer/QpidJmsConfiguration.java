package com.example.consumer;

import java.util.UUID;

import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.jms.ConnectionFactoryUnwrapper;
import org.springframework.boot.jms.autoconfigure.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration(proxyBeanMethods = false)
class QpidJmsConfiguration {

	@Bean
	CachingConnectionFactory qpidJmsConnectionFactory() {
		JmsConnectionFactory connectionFactory =
				new JmsConnectionFactory("RootManageSharedAccessKey", "SAS_KEY_VALUE", "amqp://localhost:5672");
		return new CachingConnectionFactory(connectionFactory);
	}

	@Bean
	DefaultJmsListenerContainerFactory queueJmsListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, ConnectionFactoryUnwrapper.unwrapCaching(connectionFactory));
		return factory;
	}

	@Bean
	DefaultJmsListenerContainerFactory topicJmsListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, ConnectionFactoryUnwrapper.unwrapCaching(connectionFactory));
		factory.setSubscriptionDurable(true);
		factory.setClientId(UUID.randomUUID().toString());
		return factory;
	}

}
