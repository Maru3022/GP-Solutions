package com.example.hotelapi.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContactsResponse implements Serializable {

    private final String phone;
    private final String email;
}
