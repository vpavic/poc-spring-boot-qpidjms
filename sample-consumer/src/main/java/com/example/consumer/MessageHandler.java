package com.example.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
class MessageHandler {

	private static final Logger logger = LogManager.getLogger();

	@JmsListener(destination = "sample.queue")
	void handleMessage(String message) {
		logger.info("received message: {}", message);
	}

}
