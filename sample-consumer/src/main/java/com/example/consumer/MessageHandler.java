package com.example.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
class MessageHandler {

	private static final Logger logger = LogManager.getLogger();

	@JmsListener(containerFactory = "queueJmsListenerContainerFactory", destination = "queue.1")
	void handleQueueMessage(String message) {
		logger.info("[queue.1] received message: {}", message);
	}

	@JmsListener(containerFactory = "topicJmsListenerContainerFactory", destination = "topic.1", subscription = "subscription.1")
	void handleTopicMessage(String message) {
		logger.info("[topic.1] received message: {}", message);
	}

}
