package com.gpsolutions.hotel.service;

import com.gpsolutions.hotel.dto.request.CreateHotelRequest;
import com.gpsolutions.hotel.dto.response.HotelFullResponse;
import com.gpsolutions.hotel.dto.response.HotelShortResponse;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelShortResponse> getAllHotels();

    HotelFullResponse getHotelById(Long id);

    List<HotelShortResponse> searchHotels(String name, String brand, String city, String country, List<String> amenities);

    HotelShortResponse createHotel(CreateHotelRequest request);

    HotelFullResponse addAmenitiesToHotel(Long hotelId, List<String> amenityNames);

    Map<String, Long> getHistogram(String param);
}
