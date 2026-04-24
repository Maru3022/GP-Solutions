package com.example.hotelapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelShortResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String address;
    private final String phone;
}
