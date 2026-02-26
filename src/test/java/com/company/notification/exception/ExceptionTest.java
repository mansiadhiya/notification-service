package com.company.notification.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void builder_CreatesErrorResponse() {
        Map<String, String> errors = new HashMap<>();
        errors.put("field", "error");

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Test message")
                .validationErrors(errors)
                .build();

        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getError());
        assertEquals("Test message", response.getMessage());
        assertEquals(1, response.getValidationErrors().size());
    }
}

class NotificationProcessingExceptionTest {

    @Test
    void constructor_CreatesException() {
        NotificationProcessingException ex = new NotificationProcessingException("Error");
        assertEquals("Error", ex.getMessage());
    }
}
