package com.example.hotelapi.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse implements Serializable {

    private final String houseNumber;
    private final String street;
    private final String city;
    private final String country;
    private final String postCode;
}
