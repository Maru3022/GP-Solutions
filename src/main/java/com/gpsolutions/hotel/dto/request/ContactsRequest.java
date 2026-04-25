package com.gpsolutions.hotel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting contact information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactsRequest {

    @NotBlank(message = "Phone is required")
    private String phone;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
