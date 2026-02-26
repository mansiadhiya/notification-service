package com.company.notification.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

class RabbitConfigTest {

    private final RabbitConfig config = new RabbitConfig();

    @Test
    void exchange_CreatesExchange() {
        DirectExchange exchange = config.exchange();
        assertNotNull(exchange);
        assertEquals("notification.exchange", exchange.getName());
    }

    @Test
    void deadLetterExchange_CreatesExchange() {
        DirectExchange dlx = config.deadLetterExchange();
        assertNotNull(dlx);
        assertEquals("notification.exchange.dlx", dlx.getName());
    }

    @Test
    void employeeQueue_CreatesQueue() {
        Queue queue = config.employeeQueue();
        assertNotNull(queue);
        assertEquals("employee.notification.queue", queue.getName());
    }

    @Test
    void leaveQueue_CreatesQueue() {
        Queue queue = config.leaveQueue();
        assertNotNull(queue);
        assertEquals("leave.notification.queue", queue.getName());
    }

    @Test
    void employeeDLQ_CreatesDeadLetterQueue() {
        Queue queue = config.employeeDLQ();
        assertNotNull(queue);
        assertEquals("employee.notification.queue.dlq", queue.getName());
    }

    @Test
    void leaveDLQ_CreatesDeadLetterQueue() {
        Queue queue = config.leaveDLQ();
        assertNotNull(queue);
        assertEquals("leave.notification.queue.dlq", queue.getName());
    }

    @Test
    void employeeBinding_CreatesBinding() {
        Binding binding = config.employeeBinding();
        assertNotNull(binding);
    }

    @Test
    void leaveBinding_CreatesBinding() {
        Binding binding = config.leaveBinding();
        assertNotNull(binding);
    }

    @Test
    void employeeDLQBinding_CreatesBinding() {
        Binding binding = config.employeeDLQBinding();
        assertNotNull(binding);
    }

    @Test
    void leaveDLQBinding_CreatesBinding() {
        Binding binding = config.leaveDLQBinding();
        assertNotNull(binding);
    }

    @Test
    void messageConverter_CreatesConverter() {
        assertNotNull(config.messageConverter());
    }

    @Test
    void rabbitListenerContainerFactory_CreatesFactory() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        assertNotNull(config.rabbitListenerContainerFactory(connectionFactory));
    }
}
