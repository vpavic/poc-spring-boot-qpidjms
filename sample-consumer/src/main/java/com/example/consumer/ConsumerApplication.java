package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
class ConsumerApplication {

	static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

}
