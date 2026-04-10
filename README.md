# PoC: Spring Boot Qpid JMS

[![CI](https://github.com/vpavic/poc-spring-boot-qpidjms/actions/workflows/ci.yaml/badge.svg)](https://github.com/vpavic/poc-spring-boot-qpidjms/actions/workflows/ci.yaml)

This repository contains a proof of concept for a Spring Boot module that provides auto-configuration support for [Qpid JMS](https://qpid.apache.org/components/jms/index.html).

The benefit of using Qpid JMS is that it allows using a well-established Jakarta Messaging (JMS) APIs, as by extension Spring's JMS support for [sending](https://docs.spring.io/spring-framework/reference/integration/jms/sending.html) and [receiving](https://docs.spring.io/spring-framework/reference/integration/jms/receiving.html) messages, to interact with various AMQP 1.0 message brokers such as RabbitMQ, Apache Artemis, or even Azure ServiceBus.

## Sample Applications

This repository also provides basic producer and consumer sample applications that leverage the aforementioned Qpid JMS auto-configuration and Docker Compose definitions for testing these samples against RabbitMQ, Apache Artemis or Azure ServiceBus emulator.
Depending on the selected Docker compose definition, sample applications should be started with the appropriate Spring profile (`rabbitmq`, `artemis` or `azure-servicebus`).

For example, to use samples with RabbitMQ run these commands:

```shell
$ docker compose --file compose-rabbitmq.yaml up --detach
```

```shell
$ ./gradlew :sample-producer:bootRun -Pspring.profiles.active=rabbitmq
```

```shell
$ ./gradlew :sample-consumer:bootRun -Pspring.profiles.active=rabbitmq
```

To send a test message, execute the following HTTP request:

```shell
$ curl -X POST -H 'content-type:application/json' -d '{"message":"test123"}' http://localhost:8080/message
```

This results in the following log entries:

```
2026-04-21T17:13:20.939+02:00  INFO 354898 --- [sample-producer] [nio-8080-exec-1] com.example.producer.MessageController   : sent message: test123
```

```
2026-04-21T17:13:20.955+02:00  INFO 356394 --- [sample-consumer] [ntContainer#0-1] com.example.consumer.MessageHandler      : received message: test123
```
