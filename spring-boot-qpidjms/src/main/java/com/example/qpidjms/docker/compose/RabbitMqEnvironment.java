package com.example.qpidjms.docker.compose;

import java.util.Map;

import org.jspecify.annotations.Nullable;

class RabbitMqEnvironment {

	private final @Nullable String username;

	private final @Nullable String password;

	RabbitMqEnvironment(Map<String, @Nullable String> env) {
		this.username = env.getOrDefault("RABBITMQ_DEFAULT_USER", env.getOrDefault("RABBITMQ_USERNAME", "guest"));
		this.password = env.getOrDefault("RABBITMQ_DEFAULT_PASS", env.getOrDefault("RABBITMQ_PASSWORD", "guest"));
	}

	@Nullable String getUsername() {
		return this.username;
	}

	@Nullable String getPassword() {
		return this.password;
	}

}
