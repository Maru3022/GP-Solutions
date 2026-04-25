package com.gpsolutions.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for arrival time information in response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTimeResponse {

    private String checkIn;
    private String checkOut;
}
