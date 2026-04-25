package com.gpsolutions.hotel.exception;

/**
 * Exception thrown when a hotel is not found.
 */
public class HotelNotFoundException extends RuntimeException {

    public HotelNotFoundException(String message) {
        super(message);
    }

    public HotelNotFoundException(Long id) {
        super("Hotel with id " + id + " not found");
    }
}
