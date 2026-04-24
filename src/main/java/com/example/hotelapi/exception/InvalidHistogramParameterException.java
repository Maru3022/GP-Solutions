package com.example.hotelapi.exception;

public class InvalidHistogramParameterException extends RuntimeException {

    public InvalidHistogramParameterException(String parameter) {
        super("Unsupported histogram parameter: %s. Allowed values: brand, city, country, amenities"
                .formatted(parameter));
    }
}
