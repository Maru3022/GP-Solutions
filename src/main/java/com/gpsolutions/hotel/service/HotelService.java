package com.gpsolutions.hotel.service;

import com.gpsolutions.hotel.dto.request.CreateHotelRequest;
import com.gpsolutions.hotel.dto.response.HotelFullResponse;
import com.gpsolutions.hotel.dto.response.HotelShortResponse;

import java.util.List;
import java.util.Map;

/**
 * Service interface for hotel operations.
 */
public interface HotelService {

    /**
     * Get all hotels with short information.
     */
    List<HotelShortResponse> getAllHotels();

    /**
     * Get hotel by id with full information.
     */
    HotelFullResponse getHotelById(Long id);

    /**
     * Search hotels by various criteria.
     */
    List<HotelShortResponse> searchHotels(String name, String brand, String city, String country, List<String> amenities);

    /**
     * Create a new hotel.
     */
    HotelShortResponse createHotel(CreateHotelRequest request);

    /**
     * Add amenities to a hotel.
     */
    HotelFullResponse addAmenitiesToHotel(Long hotelId, List<String> amenityNames);

    /**
     * Get histogram data grouped by parameter.
     * param can be: brand, city, country, amenities
     */
    Map<String, Long> getHistogram(String param);
}
