package com.company.notification.consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.notification.dto.EmployeeCreatedEvent;
import com.company.notification.service.EmailService;

import jakarta.validation.Validator;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class EmployeeNotificationConsumerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator eventValidator;

    @InjectMocks
    private EmployeeNotificationConsumer consumer;

    @Test
    void consume_ValidEvent_SendsEmail() throws Exception {
        String json = "{\"employeeId\":1,\"name\":\"John\",\"email\":\"john@example.com\",\"department\":1}";
        EmployeeCreatedEvent event = new EmployeeCreatedEvent();
        event.setEmployeeId(1L);
        event.setName("John");
        event.setEmail("john@example.com");
        event.setDepartment(1L);

        when(objectMapper.readValue(json, EmployeeCreatedEvent.class)).thenReturn(event);
        when(eventValidator.validate(event)).thenReturn(Collections.emptySet());

        consumer.consume(json);

        verify(emailService).sendWelcomeEmail(event);
    }

    @Test
    void consume_InvalidJson_HandlesException() throws Exception {
        String invalidJson = "invalid";

        when(objectMapper.readValue(invalidJson, EmployeeCreatedEvent.class))
            .thenThrow(new RuntimeException("Parse error"));

        consumer.consume(invalidJson);

        verify(emailService, never()).sendWelcomeEmail(any());
    }
}
