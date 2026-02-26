package com.company.notification.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LeaveStatusChangedEvent {

	@NotNull(message = "Request ID is required")
    private Long requestId;

	@NotNull(message = "Employee name is required")
    private Long employeeName;

    @NotBlank(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "End date is required")
    private String endDate;

    @NotBlank(message = "Status is required")
    private String status;

}
