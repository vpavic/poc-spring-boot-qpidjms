package com.example.producer;

import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsClient;
import org.springframework.messaging.core.DestinationResolutionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MessageController {

	private final JmsClient jmsClient;

	MessageController(JmsClient jmsClient) {
		this.jmsClient = jmsClient;
	}

	@PostMapping(path = "/{destination}")
	void home(@PathVariable String destination, @RequestBody MessageRequest request) {
		this.jmsClient.destination(destination).send(request.message);
	}

	@ExceptionHandler(DestinationResolutionException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	void handleDestinationResolutionException() {
	}

	record MessageRequest(String message) {

	}

}
