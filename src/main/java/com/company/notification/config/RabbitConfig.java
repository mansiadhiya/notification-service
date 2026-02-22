package com.company.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	public static final String EXCHANGE = "notification.exchange";

	public static final String EMPLOYEE_QUEUE = "employee.notification.queue";
	public static final String LEAVE_QUEUE = "leave.notification.queue";

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}

	@Bean
	Queue employeeQueue() {
		return new Queue(EMPLOYEE_QUEUE, true);
	}

	@Bean
	Queue leaveQueue() {
		return new Queue(LEAVE_QUEUE, true);
	}

	@Bean
	Binding employeeBinding() {
		return BindingBuilder.bind(employeeQueue()).to(exchange()).with("employee.created");
	}

	@Bean
	Binding leaveBinding() {
		return BindingBuilder.bind(leaveQueue()).to(exchange()).with("leave.status.changed");
	}

}
