package com.example.hotelapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotBlank
    @Size(max = 255)
    private String brand;

    @Valid
    @NotNull
    private AddressRequest address;

    @Valid
    @NotNull
    private ContactsRequest contacts;

    @Valid
    private ArrivalTimeRequest arrivalTime;
}
