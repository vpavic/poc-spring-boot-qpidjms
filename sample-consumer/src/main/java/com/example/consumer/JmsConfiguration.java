package com.example.consumer;

import java.util.UUID;

import jakarta.jms.ConnectionFactory;
import org.springframework.boot.jms.ConnectionFactoryUnwrapper;
import org.springframework.boot.jms.autoconfigure.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration(proxyBeanMethods = false)
class JmsConfiguration {

	@Bean
	DefaultJmsListenerContainerFactory queueJmsListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer jmsListenerContainerFactoryConfigurer,
			ConnectionFactory jmsConnectionFactory) {
		return createJmsListenerContainerFactory(jmsListenerContainerFactoryConfigurer, jmsConnectionFactory);
	}

	@Bean
	DefaultJmsListenerContainerFactory topicJmsListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer jmsListenerContainerFactoryConfigurer,
			ConnectionFactory jmsConnectionFactory) {
		DefaultJmsListenerContainerFactory jmsListenerContainerFactory =
				createJmsListenerContainerFactory(jmsListenerContainerFactoryConfigurer, jmsConnectionFactory);
		jmsListenerContainerFactory.setSubscriptionDurable(true);
		jmsListenerContainerFactory.setClientId(UUID.randomUUID().toString());
		return jmsListenerContainerFactory;
	}

	private static DefaultJmsListenerContainerFactory createJmsListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer jmsListenerContainerFactoryConfigurer,
			ConnectionFactory jmsConnectionFactory) {
		DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
		jmsListenerContainerFactoryConfigurer.configure(jmsListenerContainerFactory,
				ConnectionFactoryUnwrapper.unwrapCaching(jmsConnectionFactory));
		jmsListenerContainerFactory.setSessionAcknowledgeMode(101);
		return jmsListenerContainerFactory;
	}

}
