package com.company.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.company.notification.dto.EmployeeCreatedEvent;
import com.company.notification.dto.LeaveStatusChangedEvent;

class EmailServiceTest {

    private final EmailService emailService = new EmailService();

    @Test
    void sendWelcomeEmail_Success() {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent();
        event.setEmployeeId(1L);
        event.setName("John Doe");
        event.setEmail("john@example.com");
        event.setDepartment(1L);

        assertDoesNotThrow(() -> emailService.sendWelcomeEmail(event));
    }

    @Test
    void sendLeaveUpdateEmail_Success() {
        LeaveStatusChangedEvent event = new LeaveStatusChangedEvent();
        event.setRequestId(1L);
        event.setEmployeeName(1L);
        event.setStartDate("2024-01-01");
        event.setEndDate("2024-01-05");
        event.setStatus("APPROVED");

        assertDoesNotThrow(() -> emailService.sendLeaveUpdateEmail(event));
    }
}
