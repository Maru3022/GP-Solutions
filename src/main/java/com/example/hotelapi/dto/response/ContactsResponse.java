package com.example.hotelapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContactsResponse {

    private final String phone;
    private final String email;
}
