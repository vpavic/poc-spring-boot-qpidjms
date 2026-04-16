package com.example.producer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.JmsClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MessageController {

	private static final Logger logger = LogManager.getLogger();

	private final JmsClient jmsClient;

	MessageController(JmsClient jmsClient) {
		this.jmsClient = jmsClient;
	}

	@PostMapping(path = "/message")
	void sendMessage(@RequestBody MessageRequest request) {
		this.jmsClient.destination("sample.queue").send(request.message);
		logger.info("sent message: {}", request.message);
	}

	record MessageRequest(String message) {

	}

}
