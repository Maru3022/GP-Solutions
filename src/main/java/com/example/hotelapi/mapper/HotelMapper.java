package com.example.hotelapi.mapper;

import com.example.hotelapi.dto.request.AddressRequest;
import com.example.hotelapi.dto.request.ArrivalTimeRequest;
import com.example.hotelapi.dto.request.ContactsRequest;
import com.example.hotelapi.dto.request.HotelCreateRequest;
import com.example.hotelapi.dto.response.AddressResponse;
import com.example.hotelapi.dto.response.ArrivalTimeResponse;
import com.example.hotelapi.dto.response.ContactsResponse;
import com.example.hotelapi.dto.response.HotelDetailResponse;
import com.example.hotelapi.dto.response.HotelShortResponse;
import com.example.hotelapi.entity.Hotel;
import java.util.Comparator;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelCreateRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setBrand(request.getBrand());

        AddressRequest address = request.getAddress();
        hotel.setHouseNumber(address.getHouseNumber());
        hotel.setStreet(address.getStreet());
        hotel.setCity(address.getCity());
        hotel.setCountry(address.getCountry());
        hotel.setPostCode(address.getPostCode());

        ContactsRequest contacts = request.getContacts();
        hotel.setPhone(contacts.getPhone());
        hotel.setEmail(contacts.getEmail());

        ArrivalTimeRequest arrivalTime = request.getArrivalTime();
        if (arrivalTime != null) {
            hotel.setCheckIn(arrivalTime.getCheckIn());
            hotel.setCheckOut(arrivalTime.getCheckOut());
        }
        return hotel;
    }

    public HotelShortResponse toShortResponse(Hotel hotel) {
        return HotelShortResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(formatAddress(hotel))
                .phone(hotel.getPhone())
                .build();
    }

    public HotelDetailResponse toDetailResponse(Hotel hotel) {
        return HotelDetailResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .address(AddressResponse.builder()
                        .houseNumber(hotel.getHouseNumber())
                        .street(hotel.getStreet())
                        .city(hotel.getCity())
                        .country(hotel.getCountry())
                        .postCode(hotel.getPostCode())
                        .build())
                .contacts(ContactsResponse.builder()
                        .phone(hotel.getPhone())
                        .email(hotel.getEmail())
                        .build())
                .arrivalTime(ArrivalTimeResponse.builder()
                        .checkIn(hotel.getCheckIn())
                        .checkOut(hotel.getCheckOut())
                        .build())
                .amenities(hotel.getAmenities().stream()
                        .map(amenity -> amenity.getName())
                        .sorted(Comparator.naturalOrder())
                        .toList())
                .build();
    }

    private String formatAddress(Hotel hotel) {
        return String.format("%s %s, %s, %s, %s",
                hotel.getHouseNumber(),
                hotel.getStreet(),
                hotel.getCity(),
                hotel.getPostCode(),
                hotel.getCountry());
    }
}
