package com.company.notification.consumer;

import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.notification.config.RabbitConfig;
import com.company.notification.dto.EmployeeCreatedEvent;
import com.company.notification.exception.NotificationProcessingException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class employeeNotificationConsumer {

	 private final com.company.notification.service.emailService emailService;
	    private final ObjectMapper mapper;
	    private final Validator validator;

	    @RabbitListener(queues = "employee.notification.queue")
	    public void consume(String message) {

	        log.info("Received employee event: {}", message);

	        try {

	            EmployeeCreatedEvent event =
	                    mapper.readValue(message, EmployeeCreatedEvent.class);

	            validate(event);

	            emailService.sendWelcomeEmail(event);

	        }
	        catch (Exception e) {
	            log.error("Employee event processing failed", e);
	        }
	    }

	    private void validate(EmployeeCreatedEvent event){

	        Set<ConstraintViolation<EmployeeCreatedEvent>> violations =
	                validator.validate(event);

	        if(!violations.isEmpty())
	            throw new IllegalArgumentException(
	                    violations.iterator().next().getMessage());
	    }

}
