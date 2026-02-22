package com.company.notification.service;

import org.springframework.stereotype.Service;

import com.company.notification.dto.EmployeeCreatedEvent;
import com.company.notification.dto.LeaveStatusChangedEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class emailService {

	public void sendWelcomeEmail(EmployeeCreatedEvent event) {

		log.info("Sending Welcome Email to {} ({})", event.getName(), event.getEmail());

		simulateDelay();

		log.info("Welcome Email sent successfully for Employee ID: {}", event.getEmployeeId());
	}

	public void sendLeaveUpdateEmail(LeaveStatusChangedEvent event) {

		log.info("Sending Leave Status Email for {}", event.getEmployeeName());

		simulateDelay();

		log.info("Leave status mail sent. Request ID: {}", event.getRequestId());
	}

	private void simulateDelay() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
