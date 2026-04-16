package com.example.qpidjms.autoconfigure;

import java.net.URI;

import jakarta.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.jupiter.api.Test;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

class QpidJmsAutoConfigurationTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(QpidJmsAutoConfiguration.class, JmsAutoConfiguration.class));

	@Test
	void defaultNoConnectionFactoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.run(context -> assertThat(context)
						.doesNotHaveBean(ConnectionFactory.class)
						.doesNotHaveBean("jmsConnectionFactory"));
	}

	@Test
	void cachedConnectionFactoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672",
						"spring.qpidjms.username=username", "spring.qpidjms.password=password",
						"spring.jms.cache.consumers=true", "spring.jms.cache.producers=false",
						"spring.jms.cache.session-cache-size=10")
				.run(context -> {
					assertThat(context)
							.hasSingleBean(CachingConnectionFactory.class)
							.hasBean("jmsConnectionFactory");
					CachingConnectionFactory connectionFactory = context.getBean(CachingConnectionFactory.class);
					assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
					assertThat(connectionFactory.getTargetConnectionFactory())
							.isInstanceOfSatisfying(JmsConnectionFactory.class, targetConnectionFactory -> {
								assertThat(targetConnectionFactory.getRemoteURI()).isEqualTo("amqp://localhost:5672");
								assertThat(targetConnectionFactory.getUsername()).isEqualTo("username");
								assertThat(targetConnectionFactory.getPassword()).isEqualTo("password");
							});
					assertThat(connectionFactory.isCacheConsumers()).isTrue();
					assertThat(connectionFactory.isCacheProducers()).isFalse();
					assertThat(connectionFactory.getSessionCacheSize()).isEqualTo(10);
				});
	}

	@Test
	void nonCachedConnectionFactoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672", "spring.jms.cache.enabled=false")
				.run(context -> {
					assertThat(context)
							.hasSingleBean(ConnectionFactory.class)
							.hasBean("jmsConnectionFactory");
					JmsConnectionFactory connectionFactory = context.getBean(JmsConnectionFactory.class);
					assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
					assertThat(connectionFactory.getRemoteURI()).isEqualTo("amqp://localhost:5672");
					assertThat(connectionFactory.getUsername()).isNull();
					assertThat(connectionFactory.getPassword()).isNull();
				});
	}

	@Test
	void customizerConnectionFactoryConfiguration() {
		this.contextRunner.withUserConfiguration(CustomizerConfiguration.class)
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672")
				.run(context -> {
					assertThat(context)
							.hasSingleBean(CachingConnectionFactory.class)
							.hasBean("jmsConnectionFactory");
					CachingConnectionFactory connectionFactory = context.getBean(CachingConnectionFactory.class);
					assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
					assertThat(connectionFactory.getTargetConnectionFactory())
							.isInstanceOfSatisfying(JmsConnectionFactory.class, targetConnectionFactory -> {
								assertThat(targetConnectionFactory.getRemoteURI()).isEqualTo("amqp://localhost:5672");
								assertThat(targetConnectionFactory.getUsername()).isEqualTo("username");
								assertThat(targetConnectionFactory.getPassword()).isEqualTo("password");
							});
				});
	}

	@Test
	void configurationBacksOffWhenCustomConnectionFactoryExists() {
		this.contextRunner.withUserConfiguration(CustomConnectionFactoryConfiguration.class)
				.run(context -> assertThat(mockingDetails(context.getBean(ConnectionFactory.class)).isMock()).isTrue());
	}

	@Test
	void customConnectionDetailsConnectionFactoryConfiguration() {
		this.contextRunner.withClassLoader(new FilteredClassLoader(CachingConnectionFactory.class))
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672", "spring.jms.cache.enabled=false")
				.withUserConfiguration(TestConnectionDetailsConfiguration.class)
				.run(context -> {
					assertThat(context)
							.hasSingleBean(QpidJmsConnectionDetails.class)
							.doesNotHaveBean(QpidJmsAutoConfiguration.PropertiesQpidJmsConnectionDetails.class);
					JmsConnectionFactory connectionFactory = context.getBean(JmsConnectionFactory.class);
					assertThat(connectionFactory.getRemoteURI()).isEqualTo("amqp://localhost:5672");
					assertThat(connectionFactory.getUsername()).isEqualTo("username");
					assertThat(connectionFactory.getPassword()).isEqualTo("password");
				});
	}

	@Test
	void defaultPooledConnectionFactoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672",
						"spring.qpidjms.pool.enabled=true")
				.run(context -> {
					assertThat(context)
							.hasSingleBean(ConnectionFactory.class)
							.hasBean("jmsConnectionFactory");
					JmsPoolConnectionFactory connectionFactory = context.getBean(JmsPoolConnectionFactory.class);
					assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
					JmsPoolConnectionFactory defaultFactory = new JmsPoolConnectionFactory();
					assertThat(connectionFactory.isBlockIfSessionPoolIsFull())
							.isEqualTo(defaultFactory.isBlockIfSessionPoolIsFull());
					assertThat(connectionFactory.getBlockIfSessionPoolIsFullTimeout())
							.isEqualTo(defaultFactory.getBlockIfSessionPoolIsFullTimeout());
					assertThat(connectionFactory.getConnectionIdleTimeout())
							.isEqualTo(defaultFactory.getConnectionIdleTimeout());
					assertThat(connectionFactory.getMaxConnections()).isEqualTo(defaultFactory.getMaxConnections());
					assertThat(connectionFactory.getMaxSessionsPerConnection())
							.isEqualTo(defaultFactory.getMaxSessionsPerConnection());
					assertThat(connectionFactory.getConnectionCheckInterval())
							.isEqualTo(defaultFactory.getConnectionCheckInterval());
					assertThat(connectionFactory.isUseAnonymousProducers())
							.isEqualTo(defaultFactory.isUseAnonymousProducers());
			});
	}

	@Test
	void customPooledConnectionFactoryConfiguration() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672",
						"spring.qpidjms.pool.enabled=true", "spring.qpidjms.pool.blockIfFull=false",
						"spring.qpidjms.pool.blockIfFullTimeout=64", "spring.qpidjms.pool.idleTimeout=512",
						"spring.qpidjms.pool.maxConnections=256", "spring.qpidjms.pool.maxSessionsPerConnection=1024",
						"spring.qpidjms.pool.timeBetweenExpirationCheck=2048",
						"spring.qpidjms.pool.useAnonymousProducers=false")
				.run(context -> {
					assertThat(context)
							.hasSingleBean(ConnectionFactory.class)
							.hasBean("jmsConnectionFactory");
					JmsPoolConnectionFactory connectionFactory = context.getBean(JmsPoolConnectionFactory.class);
					assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
					assertThat(connectionFactory.isBlockIfSessionPoolIsFull()).isFalse();
					assertThat(connectionFactory.getBlockIfSessionPoolIsFullTimeout()).isEqualTo(64);
					assertThat(connectionFactory.getConnectionIdleTimeout()).isEqualTo(512);
					assertThat(connectionFactory.getMaxConnections()).isEqualTo(256);
					assertThat(connectionFactory.getMaxSessionsPerConnection()).isEqualTo(1024);
					assertThat(connectionFactory.getConnectionCheckInterval()).isEqualTo(2048);
					assertThat(connectionFactory.isUseAnonymousProducers()).isFalse();
				});
	}

	@Test
	void cachingConnectionFactoryNotOnTheClasspathThenSimpleConnectionFactoryAutoConfigured() {
		this.contextRunner.withClassLoader(new FilteredClassLoader(CachingConnectionFactory.class))
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672", "spring.jms.cache.enabled=false")
				.run(context -> {
					assertThat(context)
							.hasSingleBean(ConnectionFactory.class)
							.hasBean("jmsConnectionFactory");
					JmsConnectionFactory connectionFactory = context.getBean(JmsConnectionFactory.class);
					assertThat(context.getBean("jmsConnectionFactory")).isSameAs(connectionFactory);
				});
	}

	@Test
	void cachingConnectionFactoryNotOnTheClasspathAndCacheEnabledThenSimpleConnectionFactoryNotConfigured() {
		this.contextRunner.withClassLoader(new FilteredClassLoader(CachingConnectionFactory.class))
				.withPropertyValues("spring.qpidjms.brokerUrl=amqp://localhost:5672", "spring.jms.cache.enabled=true")
				.run(context -> assertThat(context).doesNotHaveBean(ConnectionFactory.class));
	}

	@Configuration(proxyBeanMethods = false)
	static class EmptyConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	static class CustomizerConfiguration {

		@Bean
		QpidJmsConnectionFactoryCustomizer jmsConnectionFactoryCustomizer() {
			return factory -> {
				factory.setRemoteURI("amqp://localhost:5672");
				factory.setUsername("username");
				factory.setPassword("password");
			};
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class CustomConnectionFactoryConfiguration {

		@Bean
		ConnectionFactory connectionFactory() {
			return mock(ConnectionFactory.class);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class TestConnectionDetailsConfiguration {

		@Bean
		QpidJmsConnectionDetails qpidJmsConnectionDetails() {
			return new QpidJmsConnectionDetails() {

				@Override
				public URI getBrokerUrl() {
					return URI.create("amqp://localhost:5672");
				}

				@Override
				public String getUsername() {
					return "username";
				}

				@Override
				public String getPassword() {
					return "password";
				}

			};
		}

	}

}
