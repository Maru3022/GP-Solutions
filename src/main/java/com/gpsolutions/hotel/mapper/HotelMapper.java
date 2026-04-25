package com.gpsolutions.hotel.mapper;

import com.gpsolutions.hotel.dto.request.AddressRequest;
import com.gpsolutions.hotel.dto.request.ArrivalTimeRequest;
import com.gpsolutions.hotel.dto.request.ContactsRequest;
import com.gpsolutions.hotel.dto.request.CreateHotelRequest;
import com.gpsolutions.hotel.dto.response.AddressResponse;
import com.gpsolutions.hotel.dto.response.ArrivalTimeResponse;
import com.gpsolutions.hotel.dto.response.ContactsResponse;
import com.gpsolutions.hotel.dto.response.HotelFullResponse;
import com.gpsolutions.hotel.dto.response.HotelShortResponse;
import com.gpsolutions.hotel.entity.Address;
import com.gpsolutions.hotel.entity.ArrivalTime;
import com.gpsolutions.hotel.entity.Contacts;
import com.gpsolutions.hotel.entity.Hotel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Hotel entities and DTOs.
 */
@Component
public class HotelMapper {

    /**
     * Convert Hotel entity to HotelShortResponse.
     * Address is formatted as a single string.
     */
    public HotelShortResponse toShortResponse(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        String addressString = formatAddressToString(hotel.getAddress());

        return HotelShortResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(addressString)
                .phone(hotel.getContacts() != null ? hotel.getContacts().getPhone() : null)
                .build();
    }

    /**
     * Convert Hotel entity to HotelFullResponse.
     */
    public HotelFullResponse toFullResponse(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        return HotelFullResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .address(toAddressResponse(hotel.getAddress()))
                .contacts(toContactsResponse(hotel.getContacts()))
                .arrivalTime(toArrivalTimeResponse(hotel.getArrivalTime()))
                .amenities(hotel.getAmenities() != null ? hotel.getAmenities().stream()
                        .map(amenity -> amenity.getName())
                        .collect(Collectors.toList()) : List.of())
                .build();
    }

    /**
     * Convert CreateHotelRequest to Hotel entity.
     */
    public Hotel toEntity(CreateHotelRequest request) {
        if (request == null) {
            return null;
        }

        return Hotel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .brand(request.getBrand())
                .address(toAddressEntity(request.getAddress()))
                .contacts(toContactsEntity(request.getContacts()))
                .arrivalTime(toArrivalTimeEntity(request.getArrivalTime()))
                .build();
    }

    /**
     * Convert Address entity to AddressResponse.
     */
    public AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }

        return AddressResponse.builder()
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .postCode(address.getPostCode())
                .build();
    }

    /**
     * Convert AddressRequest to Address entity.
     */
    public Address toAddressEntity(AddressRequest request) {
        if (request == null) {
            return null;
        }

        return Address.builder()
                .houseNumber(request.getHouseNumber())
                .street(request.getStreet())
                .city(request.getCity())
                .country(request.getCountry())
                .postCode(request.getPostCode())
                .build();
    }

    /**
     * Convert Contacts entity to ContactsResponse.
     */
    public ContactsResponse toContactsResponse(Contacts contacts) {
        if (contacts == null) {
            return null;
        }

        return ContactsResponse.builder()
                .phone(contacts.getPhone())
                .email(contacts.getEmail())
                .build();
    }

    /**
     * Convert ContactsRequest to Contacts entity.
     */
    public Contacts toContactsEntity(ContactsRequest request) {
        if (request == null) {
            return null;
        }

        return Contacts.builder()
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    /**
     * Convert ArrivalTime entity to ArrivalTimeResponse.
     */
    public ArrivalTimeResponse toArrivalTimeResponse(ArrivalTime arrivalTime) {
        if (arrivalTime == null) {
            return null;
        }

        return ArrivalTimeResponse.builder()
                .checkIn(arrivalTime.getCheckIn())
                .checkOut(arrivalTime.getCheckOut())
                .build();
    }

    /**
     * Convert ArrivalTimeRequest to ArrivalTime entity.
     */
    public ArrivalTime toArrivalTimeEntity(ArrivalTimeRequest request) {
        if (request == null) {
            return null;
        }

        return ArrivalTime.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .build();
    }

    /**
     * Format address to a single string.
     * Format: "houseNumber street, city, postCode, country"
     */
    public String formatAddressToString(Address address) {
        if (address == null) {
            return null;
        }

        return String.format("%s %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }
}
