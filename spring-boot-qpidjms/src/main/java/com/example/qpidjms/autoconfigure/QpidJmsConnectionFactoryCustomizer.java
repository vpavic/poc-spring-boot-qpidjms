package com.example.qpidjms.autoconfigure;

import org.apache.qpid.jms.JmsConnectionFactory;

@FunctionalInterface
public interface QpidJmsConnectionFactoryCustomizer {

	void customize(JmsConnectionFactory connectionFactory);

}
