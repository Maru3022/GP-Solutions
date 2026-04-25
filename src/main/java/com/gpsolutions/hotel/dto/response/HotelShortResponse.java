package com.gpsolutions.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for short hotel information.
 * Used in list endpoints.
 * Contains: id, name, description, address (as single line string), phone
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelShortResponse {

    private Long id;
    private String name;
    private String description;
    private String address; // Single line: "9 Pobediteley Avenue, Minsk, 220004, Belarus"
    private String phone;
}
