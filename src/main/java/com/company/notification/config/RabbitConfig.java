package com.company.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	public static final String EXCHANGE = "notification.exchange";
	public static final String DLX = "notification.exchange.dlx";

	public static final String EMPLOYEE_QUEUE = "employee.notification.queue";
	public static final String LEAVE_QUEUE = "leave.notification.queue";
	public static final String EMPLOYEE_DLQ = "employee.notification.queue.dlq";
	public static final String LEAVE_DLQ = "leave.notification.queue.dlq";

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}
	
	@Bean
	DirectExchange deadLetterExchange() {
		return new DirectExchange(DLX);
	}

	@Bean
	Queue employeeQueue() {
		return QueueBuilder.durable(EMPLOYEE_QUEUE)
				.withArgument("x-dead-letter-exchange", DLX)
				.withArgument("x-dead-letter-routing-key", "employee.created.dlq")
				.build();
	}

	@Bean
	Queue leaveQueue() {
		return QueueBuilder.durable(LEAVE_QUEUE)
				.withArgument("x-dead-letter-exchange", DLX)
				.withArgument("x-dead-letter-routing-key", "leave.status.changed.dlq")
				.build();
	}
	
	@Bean
	Queue employeeDLQ() {
		return QueueBuilder.durable(EMPLOYEE_DLQ).build();
	}
	
	@Bean
	Queue leaveDLQ() {
		return QueueBuilder.durable(LEAVE_DLQ).build();
	}

	@Bean
	Binding employeeBinding() {
		return BindingBuilder.bind(employeeQueue()).to(exchange()).with("employee.created");
	}

	@Bean
	Binding leaveBinding() {
		return BindingBuilder.bind(leaveQueue()).to(exchange()).with("leave.status.changed");
	}
	
	@Bean
	Binding employeeDLQBinding() {
		return BindingBuilder.bind(employeeDLQ()).to(deadLetterExchange()).with("employee.created.dlq");
	}
	
	@Bean
	Binding leaveDLQBinding() {
		return BindingBuilder.bind(leaveDLQ()).to(deadLetterExchange()).with("leave.status.changed.dlq");
	}
	
	@Bean
	Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter());
		factory.setDefaultRequeueRejected(false);
		return factory;
	}
}
