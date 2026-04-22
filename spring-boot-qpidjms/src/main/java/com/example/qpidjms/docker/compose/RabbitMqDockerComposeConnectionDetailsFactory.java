package com.example.qpidjms.docker.compose;

import java.net.URI;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

import com.example.qpidjms.autoconfigure.QpidJmsConnectionDetails;

class RabbitMqDockerComposeConnectionDetailsFactory
		extends DockerComposeConnectionDetailsFactory<QpidJmsConnectionDetails> {

	private static final int RABBITMQ_PORT = 5672;

	protected RabbitMqDockerComposeConnectionDetailsFactory() {
		super("rabbitmq");
	}

	@Override
	protected @Nullable QpidJmsConnectionDetails getDockerComposeConnectionDetails(
			DockerComposeConnectionSource source) {
		try {
			return new RabbitMqDockerComposeConnectionDetails(source.getRunningService());
		}
		catch (IllegalStateException ex) {
			return null;
		}
	}

	static class RabbitMqDockerComposeConnectionDetails extends DockerComposeConnectionDetails
			implements QpidJmsConnectionDetails {

		private final RabbitMqEnvironment environment;

		private final URI brokerUrl;

		protected RabbitMqDockerComposeConnectionDetails(RunningService service) {
			super(service);
			this.environment = new RabbitMqEnvironment(service.env());
			this.brokerUrl = URI.create("amqp://%s:%d".formatted(service.host(), service.ports().get(RABBITMQ_PORT)));
		}

		@Override
		public URI getBrokerUrl() {
			return this.brokerUrl;
		}

		@Override
		public @Nullable String getUsername() {
			return this.environment.getUsername();
		}

		@Override
		public @Nullable String getPassword() {
			return this.environment.getPassword();
		}

	}

}
