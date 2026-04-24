package com.example.hotelapi.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelDetailResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String brand;
    private final AddressResponse address;
    private final ContactsResponse contacts;
    private final ArrivalTimeResponse arrivalTime;
    private final List<String> amenities;
}
