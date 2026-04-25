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
import com.gpsolutions.hotel.entity.Amenity;
import com.gpsolutions.hotel.entity.ArrivalTime;
import com.gpsolutions.hotel.entity.Contacts;
import com.gpsolutions.hotel.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for HotelMapper.
 */
class HotelMapperTest {

    private HotelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new HotelMapper();
    }

    @Test
    void toShortResponse_shouldMapCorrectly() {
        Address address = Address.builder()
                .houseNumber("10")
                .street("Main St")
                .city("Moscow")
                .postCode("123456")
                .country("Russia")
                .build();
        Contacts contacts = Contacts.builder().phone("+7999").build();
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Hilton")
                .description("Luxury")
                .address(address)
                .contacts(contacts)
                .build();

        HotelShortResponse response = mapper.toShortResponse(hotel);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Hilton");
        assertThat(response.getAddress()).isEqualTo("10 Main St, Moscow, 123456, Russia");
        assertThat(response.getPhone()).isEqualTo("+7999");
    }

    @Test
    void toShortResponse_shouldReturnNull_whenInputNull() {
        assertThat(mapper.toShortResponse(null)).isNull();
    }

    @Test
    void toFullResponse_shouldMapCorrectly() {
        Address address = Address.builder().houseNumber("1").street("St").city("City").country("Country").postCode("000").build();
        Contacts contacts = Contacts.builder().phone("+1").email("a@b.com").build();
        ArrivalTime arrivalTime = ArrivalTime.builder().checkIn("14:00").checkOut("12:00").build();
        Amenity amenity = Amenity.builder().name("WiFi").build();
        Hotel hotel = Hotel.builder()
                .id(2L)
                .name("Marriott")
                .brand("Marriott")
                .description("Nice")
                .address(address)
                .contacts(contacts)
                .arrivalTime(arrivalTime)
                .amenities(Set.of(amenity))
                .build();

        HotelFullResponse response = mapper.toFullResponse(hotel);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getName()).isEqualTo("Marriott");
        assertThat(response.getAmenities()).containsExactly("WiFi");
        assertThat(response.getAddress().getCity()).isEqualTo("City");
        assertThat(response.getContacts().getPhone()).isEqualTo("+1");
        assertThat(response.getArrivalTime().getCheckIn()).isEqualTo("14:00");
    }

    @Test
    void toFullResponse_shouldReturnNull_whenInputNull() {
        assertThat(mapper.toFullResponse(null)).isNull();
    }

    @Test
    void toEntity_shouldMapCorrectly() {
        CreateHotelRequest request = CreateHotelRequest.builder()
                .name("New")
                .description("Desc")
                .brand("Brand")
                .address(AddressRequest.builder().houseNumber("1").street("St").city("City").country("Country").postCode("000").build())
                .contacts(ContactsRequest.builder().phone("+1").email("a@b.com").build())
                .arrivalTime(ArrivalTimeRequest.builder().checkIn("14:00").checkOut("12:00").build())
                .build();

        Hotel hotel = mapper.toEntity(request);

        assertThat(hotel.getName()).isEqualTo("New");
        assertThat(hotel.getAddress().getCity()).isEqualTo("City");
        assertThat(hotel.getContacts().getPhone()).isEqualTo("+1");
        assertThat(hotel.getArrivalTime().getCheckIn()).isEqualTo("14:00");
    }

    @Test
    void toEntity_shouldReturnNull_whenInputNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toAddressResponse_shouldMapCorrectly() {
        Address address = Address.builder().houseNumber("1").street("St").city("City").country("Country").postCode("000").build();
        AddressResponse response = mapper.toAddressResponse(address);
        assertThat(response.getCity()).isEqualTo("City");
    }

    @Test
    void toAddressResponse_shouldReturnNull_whenInputNull() {
        assertThat(mapper.toAddressResponse(null)).isNull();
    }

    @Test
    void toContactsResponse_shouldMapCorrectly() {
        Contacts contacts = Contacts.builder().phone("+1").email("a@b.com").build();
        ContactsResponse response = mapper.toContactsResponse(contacts);
        assertThat(response.getPhone()).isEqualTo("+1");
        assertThat(response.getEmail()).isEqualTo("a@b.com");
    }

    @Test
    void toContactsResponse_shouldReturnNull_whenInputNull() {
        assertThat(mapper.toContactsResponse(null)).isNull();
    }

    @Test
    void toArrivalTimeResponse_shouldMapCorrectly() {
        ArrivalTime arrivalTime = ArrivalTime.builder().checkIn("14:00").checkOut("12:00").build();
        ArrivalTimeResponse response = mapper.toArrivalTimeResponse(arrivalTime);
        assertThat(response.getCheckIn()).isEqualTo("14:00");
        assertThat(response.getCheckOut()).isEqualTo("12:00");
    }

    @Test
    void toArrivalTimeResponse_shouldReturnNull_whenInputNull() {
        assertThat(mapper.toArrivalTimeResponse(null)).isNull();
    }

    @Test
    void formatAddressToString_shouldFormatCorrectly() {
        Address address = Address.builder()
                .houseNumber("10")
                .street("Main St")
                .city("Moscow")
                .postCode("123456")
                .country("Russia")
                .build();
        assertThat(mapper.formatAddressToString(address)).isEqualTo("10 Main St, Moscow, 123456, Russia");
    }

    @Test
    void formatAddressToString_shouldReturnNull_whenInputNull() {
        assertThat(mapper.formatAddressToString(null)).isNull();
    }
}
