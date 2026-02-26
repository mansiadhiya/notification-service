package com.company.notification.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LeaveStatusChangedEventTest {

    @Test
    void setAndGetFields() {
        LeaveStatusChangedEvent event = new LeaveStatusChangedEvent();
        event.setRequestId(1L);
        event.setEmployeeName(1L);
        event.setStartDate("2024-01-01");
        event.setEndDate("2024-01-05");
        event.setStatus("APPROVED");

        assertEquals(1L, event.getRequestId());
        assertEquals(1L, event.getEmployeeName());
        assertEquals("2024-01-01", event.getStartDate());
        assertEquals("2024-01-05", event.getEndDate());
        assertEquals("APPROVED", event.getStatus());
    }
}
