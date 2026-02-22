package com.company.notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeCreatedEvent {

	@NotNull(message="Employee ID is required")
    private Long employeeId;

    @NotBlank(message="Name is required")
    private String name;

    @Email
    @NotBlank(message="Email is required")
    private String email;

    @NotNull(message="Department is required")
    private Long department;

}
