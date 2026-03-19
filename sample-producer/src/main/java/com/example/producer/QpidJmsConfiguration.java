package com.example.producer;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration(proxyBeanMethods = false)
class QpidJmsConfiguration {

	@Bean
	CachingConnectionFactory qpidJmsConnectionFactory() {
		JmsConnectionFactory connectionFactory =
				new JmsConnectionFactory("RootManageSharedAccessKey", "SAS_KEY_VALUE", "amqp://localhost:5672");
		return new CachingConnectionFactory(connectionFactory);
	}

}
