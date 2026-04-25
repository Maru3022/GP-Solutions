package com.gpsolutions.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for full hotel information.
 * Used in detail endpoints.
 * Contains: id, name, description, brand, address (as object), contacts, arrivalTime, amenities (list)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelFullResponse {

    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressResponse address;
    private ContactsResponse contacts;
    private ArrivalTimeResponse arrivalTime;
    private List<String> amenities;
}
