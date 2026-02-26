package com.company.notification.consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.notification.dto.LeaveStatusChangedEvent;
import com.company.notification.service.EmailService;

import jakarta.validation.Validator;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class LeaveNotificationConsumerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator eventValidator;

    @InjectMocks
    private LeaveNotificationConsumer consumer;

    @Test
    void consume_ValidEvent_SendsEmail() throws Exception {
        String json = "{\"requestId\":1,\"employeeName\":1,\"startDate\":\"2024-01-01\",\"endDate\":\"2024-01-05\",\"status\":\"APPROVED\"}";
        LeaveStatusChangedEvent event = new LeaveStatusChangedEvent();
        event.setRequestId(1L);
        event.setEmployeeName(1L);
        event.setStartDate("2024-01-01");
        event.setEndDate("2024-01-05");
        event.setStatus("APPROVED");

        when(objectMapper.readValue(json, LeaveStatusChangedEvent.class)).thenReturn(event);
        when(eventValidator.validate(event)).thenReturn(Collections.emptySet());

        consumer.consume(json);

        verify(emailService).sendLeaveUpdateEmail(event);
    }

    @Test
    void consume_InvalidJson_HandlesException() throws Exception {
        String invalidJson = "invalid";

        when(objectMapper.readValue(invalidJson, LeaveStatusChangedEvent.class))
            .thenThrow(new RuntimeException("Parse error"));

        consumer.consume(invalidJson);

        verify(emailService, never()).sendLeaveUpdateEmail(any());
    }
}
