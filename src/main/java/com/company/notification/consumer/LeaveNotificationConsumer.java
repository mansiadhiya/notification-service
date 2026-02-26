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

	private final com.company.notification.service.EmailService emailService;
    private final ObjectMapper objectMapper;
    private final Validator eventValidator;

    @RabbitListener(queues = "leave.notification.queue")
    public void consume(String messageContent){

        log.info("Received leave event: {}", messageContent);

        try{

            LeaveStatusChangedEvent leaveEvent =
                    objectMapper.readValue(messageContent, LeaveStatusChangedEvent.class);

            validate(leaveEvent);

            emailService.sendLeaveUpdateEmail(leaveEvent);

        } catch (Exception processingException){
            log.error("Leave message processing failed", processingException);
        }
    }

    private void validate(LeaveStatusChangedEvent leaveEvent){

        Set<ConstraintViolation<LeaveStatusChangedEvent>> validationViolations =
                eventValidator.validate(leaveEvent);

        if(!validationViolations.isEmpty())
            throw new IllegalArgumentException(
                    validationViolations.iterator().next().getMessage());
    }

}
