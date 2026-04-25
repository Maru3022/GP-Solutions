package com.gpsolutions.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting arrival time information.
 * Format: "HH:mm" (e.g., "15:00")
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTimeRequest {

    @NotBlank(message = "Check-in time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", 
            message = "Check-in time must be in HH:mm format")
    private String checkIn;

    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", 
            message = "Check-out time must be in HH:mm format")
    private String checkOut;
}
