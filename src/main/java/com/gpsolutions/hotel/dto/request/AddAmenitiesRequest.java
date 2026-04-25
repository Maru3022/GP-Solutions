package com.gpsolutions.hotel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for adding amenities to a hotel.
 * Request body should be a JSON array of amenity names.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAmenitiesRequest {

    @NotEmpty(message = "Amenities list cannot be empty")
    private List<String> amenities;
}
