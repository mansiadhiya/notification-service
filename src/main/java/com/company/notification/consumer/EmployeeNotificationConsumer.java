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
public class EmployeeNotificationConsumer {

	 private final com.company.notification.service.EmailService emailService;
	    private final ObjectMapper objectMapper;
	    private final Validator eventValidator;

	    @RabbitListener(queues = "employee.notification.queue")
	    public void consume(String messageContent) {

	        log.info("Received employee event: {}", messageContent);

	        try {

	            EmployeeCreatedEvent employeeEvent =
	                    objectMapper.readValue(messageContent, EmployeeCreatedEvent.class);

	            validate(employeeEvent);

	            emailService.sendWelcomeEmail(employeeEvent);

	        }
	        catch (Exception processingException) {
	            log.error("Employee event processing failed", processingException);
	        }
	    }

	    private void validate(EmployeeCreatedEvent employeeEvent){

	        Set<ConstraintViolation<EmployeeCreatedEvent>> validationViolations =
	                eventValidator.validate(employeeEvent);

	        if(!validationViolations.isEmpty())
	            throw new IllegalArgumentException(
	                    validationViolations.iterator().next().getMessage());
	    }

}
