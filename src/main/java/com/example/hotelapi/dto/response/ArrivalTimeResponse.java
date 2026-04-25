package com.example.hotelapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArrivalTimeResponse implements Serializable {

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime checkOut;
}
