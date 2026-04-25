package com.gpsolutions.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for contact information in response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactsResponse {

    private String phone;
    private String email;
}
