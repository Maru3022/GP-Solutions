package com.example.hotelapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.hotelapi.dto.request.AddressRequest;
import com.example.hotelapi.dto.request.ContactsRequest;
import com.example.hotelapi.dto.request.HotelCreateRequest;
import com.example.hotelapi.entity.Amenity;
import com.example.hotelapi.entity.Hotel;
import com.example.hotelapi.exception.HotelNotFoundException;
import com.example.hotelapi.exception.InvalidHistogramParameterException;
import com.example.hotelapi.mapper.HotelMapper;
import com.example.hotelapi.repository.AmenityRepository;
import com.example.hotelapi.repository.HotelRepository;
import com.example.hotelapi.service.impl.HotelServiceImpl;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    private final HotelMapper hotelMapper = new HotelMapper();

    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelServiceImpl(hotelRepository, amenityRepository, hotelMapper);
    }

    @Test
    void shouldReturnHotelById() {
        Hotel hotel = createHotel();
        when(hotelRepository.findWithAmenitiesById(1L)).thenReturn(Optional.of(hotel));

        var response = hotelService.getById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("DoubleTree by Hilton Minsk");
        assertThat(response.getAmenities()).contains("Free WiFi");
    }

    @Test
    void shouldThrowWhenHotelDoesNotExist() {
        when(hotelRepository.findWithAmenitiesById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getById(100L))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining("100");
    }

    @Test
    void shouldCreateHotel() {
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName("New Hotel");
        request.setBrand("Brand");

        AddressRequest address = new AddressRequest();
        address.setHouseNumber("1");
        address.setStreet("Main Street");
        address.setCity("Minsk");
        address.setCountry("Belarus");
        address.setPostCode("220000");
        request.setAddress(address);

        ContactsRequest contacts = new ContactsRequest();
        contacts.setPhone("+375 29 000-00-00");
        contacts.setEmail("new@hotel.com");
        request.setContacts(contacts);

        Hotel savedHotel = hotelMapper.toEntity(request);
        savedHotel.setId(10L);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(savedHotel);

        var response = hotelService.create(request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getAddress()).contains("Main Street");
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void shouldAddAmenitiesToHotel() {
        Hotel hotel = createHotel();

        when(hotelRepository.findWithAmenitiesById(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByNameIgnoreCase("Spa")).thenReturn(Optional.empty());
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(amenityRepository.save(any(Amenity.class))).thenAnswer(invocation -> {
            Amenity amenity = invocation.getArgument(0);
            amenity.setId(2L);
            return amenity;
        });

        var response = hotelService.addAmenities(1L, List.of("Spa"));

        assertThat(response.getAmenities()).contains("Spa", "Free WiFi");
        verify(amenityRepository).save(any(Amenity.class));
    }

    @Test
    void shouldReturnHistogramByCity() {
        when(hotelRepository.countByCity()).thenReturn(List.of(
                new Object[]{"Minsk", 2L},
                new Object[]{"Brest", 1L}
        ));

        var histogram = hotelService.getHistogram("city");

        assertThat(histogram).containsEntry("Brest", 1L);
        assertThat(histogram).containsEntry("Minsk", 2L);
    }

    @Test
    void shouldThrowForUnsupportedHistogram() {
        assertThatThrownBy(() -> hotelService.getHistogram("invalid"))
                .isInstanceOf(InvalidHistogramParameterException.class);
    }

    private Hotel createHotel() {
        Amenity amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName("Free WiFi");

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("DoubleTree by Hilton Minsk");
        hotel.setDescription("Description");
        hotel.setBrand("Hilton");
        hotel.setHouseNumber("9");
        hotel.setStreet("Pobediteley Avenue");
        hotel.setCity("Minsk");
        hotel.setCountry("Belarus");
        hotel.setPostCode("220004");
        hotel.setPhone("+375 17 309-80-00");
        hotel.setEmail("doubletreeminsk.info@hilton.com");
        hotel.setCheckIn(LocalTime.of(14, 0));
        hotel.setCheckOut(LocalTime.of(12, 0));
        hotel.setAmenities(new LinkedHashSet<>(Set.of(amenity)));
        return hotel;
    }
}
