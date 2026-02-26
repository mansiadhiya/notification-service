package com.company.notification.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeCreatedEventTest {

    @Test
    void setAndGetFields() {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent();
        event.setEmployeeId(1L);
        event.setName("John Doe");
        event.setEmail("john@example.com");
        event.setDepartment(1L);

        assertEquals(1L, event.getEmployeeId());
        assertEquals("John Doe", event.getName());
        assertEquals("john@example.com", event.getEmail());
        assertEquals(1L, event.getDepartment());
    }
}
