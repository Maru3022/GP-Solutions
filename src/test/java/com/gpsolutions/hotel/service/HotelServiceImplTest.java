package com.gpsolutions.hotel.service;

import com.gpsolutions.hotel.dto.request.AddressRequest;
import com.gpsolutions.hotel.dto.request.ArrivalTimeRequest;
import com.gpsolutions.hotel.dto.request.ContactsRequest;
import com.gpsolutions.hotel.dto.request.CreateHotelRequest;
import com.gpsolutions.hotel.dto.response.HotelFullResponse;
import com.gpsolutions.hotel.dto.response.HotelShortResponse;
import com.gpsolutions.hotel.entity.Address;
import com.gpsolutions.hotel.entity.Amenity;
import com.gpsolutions.hotel.entity.Contacts;
import com.gpsolutions.hotel.entity.Hotel;
import com.gpsolutions.hotel.exception.HotelNotFoundException;
import com.gpsolutions.hotel.mapper.HotelMapper;
import com.gpsolutions.hotel.repository.AmenityRepository;
import com.gpsolutions.hotel.repository.HotelRepository;
import com.gpsolutions.hotel.repository.HotelSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HotelServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelShortResponse shortResponse;
    private HotelFullResponse fullResponse;

    @BeforeEach
    void setUp() {
        hotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .brand("Test Brand")
                .build();

        shortResponse = HotelShortResponse.builder()
                .id(1L)
                .name("Test Hotel")
                .build();

        fullResponse = HotelFullResponse.builder()
                .id(1L)
                .name("Test Hotel")
                .brand("Test Brand")
                .build();
    }

    @Test
    void getAllHotels_shouldReturnList() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toShortResponse(hotel)).thenReturn(shortResponse);

        List<HotelShortResponse> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Hotel");
        verify(hotelRepository).findAll();
    }

    @Test
    void getHotelById_shouldReturnFullResponse() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toFullResponse(hotel)).thenReturn(fullResponse);

        HotelFullResponse result = hotelService.getHotelById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(hotelRepository).findById(1L);
    }

    @Test
    void getHotelById_shouldThrow_whenNotFound() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.getHotelById(999L));
    }

    @Test
    void searchHotels_shouldReturnFilteredResults() {
        when(hotelRepository.findAll(any(HotelSpecification.class))).thenReturn(List.of(hotel));
        when(hotelMapper.toShortResponse(hotel)).thenReturn(shortResponse);

        List<HotelShortResponse> result = hotelService.searchHotels("Test", null, null, null, null);

        assertThat(result).hasSize(1);
        verify(hotelRepository).findAll(any(HotelSpecification.class));
    }

    @Test
    void createHotel_shouldSaveAndReturnShortResponse() {
        CreateHotelRequest request = CreateHotelRequest.builder()
                .name("New Hotel")
                .brand("New Brand")
                .address(AddressRequest.builder().houseNumber("1").street("St").city("City").country("Country").postCode("123").build())
                .contacts(ContactsRequest.builder().phone("+123").email("test@test.com").build())
                .arrivalTime(ArrivalTimeRequest.builder().checkIn("14:00").checkOut("12:00").build())
                .build();

        Hotel newHotel = Hotel.builder().name("New Hotel").build();
        Hotel savedHotel = Hotel.builder().id(10L).name("New Hotel").build();

        when(hotelMapper.toEntity(request)).thenReturn(newHotel);
        when(hotelRepository.save(newHotel)).thenReturn(savedHotel);
        when(hotelMapper.toShortResponse(savedHotel)).thenReturn(HotelShortResponse.builder().id(10L).name("New Hotel").build());

        HotelShortResponse result = hotelService.createHotel(request);

        assertThat(result.getId()).isEqualTo(10L);
        verify(hotelRepository).save(newHotel);
    }

    @Test
    void addAmenitiesToHotel_shouldAddNewAmenities() {
        Amenity wifi = Amenity.builder().id(1L).name("Free Wi-Fi").build();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByName("Free Wi-Fi")).thenReturn(Optional.empty());
        when(amenityRepository.save(any(Amenity.class))).thenReturn(wifi);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toFullResponse(hotel)).thenReturn(fullResponse);

        HotelFullResponse result = hotelService.addAmenitiesToHotel(1L, List.of("Free Wi-Fi"));

        assertThat(result).isNotNull();
        ArgumentCaptor<Hotel> hotelCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository).save(hotelCaptor.capture());
        assertThat(hotelCaptor.getValue().getAmenities()).containsExactly(wifi);
    }

    @Test
    void getHistogramByBrand_shouldReturnMap() {
        when(hotelRepository.countByBrand()).thenReturn(List.<Object[]>of(new Object[]{"BrandA", 5L}));

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertThat(result).containsEntry("BrandA", 5L);
    }

    @Test
    void getHistogramByCity_shouldReturnMap() {
        when(hotelRepository.countByCity()).thenReturn(List.<Object[]>of(new Object[]{"Moscow", 3L}));

        Map<String, Long> result = hotelService.getHistogram("city");

        assertThat(result).containsEntry("Moscow", 3L);
    }

    @Test
    void getHistogramByCountry_shouldReturnMap() {
        when(hotelRepository.countByCountry()).thenReturn(List.<Object[]>of(new Object[]{"Russia", 2L}));

        Map<String, Long> result = hotelService.getHistogram("country");

        assertThat(result).containsEntry("Russia", 2L);
    }

    @Test
    void getHistogramByAmenities_shouldReturnMap() {
        when(hotelRepository.countByAmenities()).thenReturn(List.<Object[]>of(new Object[]{"WiFi", 4L}));

        Map<String, Long> result = hotelService.getHistogram("amenities");

        assertThat(result).containsEntry("WiFi", 4L);
    }

    @Test
    void getHistogram_shouldThrow_whenInvalidParam() {
        assertThrows(IllegalArgumentException.class, () -> hotelService.getHistogram("invalid"));
    }
}
