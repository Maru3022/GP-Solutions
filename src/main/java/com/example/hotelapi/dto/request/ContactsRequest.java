package com.example.hotelapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactsRequest {

    @NotBlank
    @Size(max = 100)
    private String phone;

    @Email
    @NotBlank
    @Size(max = 255)
    private String email;
}
