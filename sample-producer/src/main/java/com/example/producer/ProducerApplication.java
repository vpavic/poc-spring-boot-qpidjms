package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
class ProducerApplication {

	static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

}
