package com.company.notification.consumer;

import java.util.Set;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.notification.config.RabbitConfig;
import com.company.notification.dto.LeaveStatusChangedEvent;
import com.company.notification.exception.NotificationProcessingException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaveNotificationConsumer {

	private final com.company.notification.service.emailService emailService;
    private final ObjectMapper mapper;
    private final Validator validator;

    @RabbitListener(queues = "leave.notification.queue")
    public void consume(String message){

        log.info("Received leave event: {}", message);

        try{

            LeaveStatusChangedEvent event =
                    mapper.readValue(message, LeaveStatusChangedEvent.class);

            validate(event);

            emailService.sendLeaveUpdateEmail(event);

        } catch (Exception e){
            log.error("Leave message processing failed", e);
        }
    }

    private void validate(LeaveStatusChangedEvent event){

        Set<ConstraintViolation<LeaveStatusChangedEvent>> violations =
                validator.validate(event);

        if(!violations.isEmpty())
            throw new IllegalArgumentException(
                    violations.iterator().next().getMessage());
    }

}
