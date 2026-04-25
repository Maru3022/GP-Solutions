package com.gpsolutions.hotel.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new hotel.
 * All required fields must be provided.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHotelRequest {

    @NotBlank(message = "Hotel name is required")
    private String name;

    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequest address;

    @Valid
    @NotNull(message = "Contacts are required")
    private ContactsRequest contacts;

    @Valid
    @NotNull(message = "Arrival time is required")
    private ArrivalTimeRequest arrivalTime;
}
