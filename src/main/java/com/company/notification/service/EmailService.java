package com.company.notification.service;

import org.springframework.stereotype.Service;

import com.company.notification.dto.EmployeeCreatedEvent;
import com.company.notification.dto.LeaveStatusChangedEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	public void sendWelcomeEmail(EmployeeCreatedEvent employeeEvent) {

		log.info("Sending Welcome Email to {} ({})", employeeEvent.getName(), employeeEvent.getEmail());

		simulateDelay();

		log.info("Welcome Email sent successfully for Employee ID: {}", employeeEvent.getEmployeeId());
	}

	public void sendLeaveUpdateEmail(LeaveStatusChangedEvent leaveEvent) {

		log.info("Sending Leave Status Email for {}", leaveEvent.getEmployeeName());

		simulateDelay();

		log.info("Leave status mail sent. Request ID: {}", leaveEvent.getRequestId());
	}

	private void simulateDelay() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
