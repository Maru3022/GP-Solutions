package com.example.hotelapi.service;

import com.example.hotelapi.dto.request.HotelCreateRequest;
import com.example.hotelapi.dto.response.HotelDetailResponse;
import com.example.hotelapi.dto.response.HotelShortResponse;
import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelShortResponse> getAll();

    HotelDetailResponse getById(Long id);

    List<HotelShortResponse> search(String name, String brand, String city, String country, List<String> amenities);

    HotelShortResponse create(HotelCreateRequest request);

    HotelDetailResponse addAmenities(Long hotelId, List<String> amenities);

    Map<String, Long> getHistogram(String param);
}
