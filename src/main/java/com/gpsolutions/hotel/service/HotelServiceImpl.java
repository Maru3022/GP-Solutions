package com.gpsolutions.hotel.service;

import com.gpsolutions.hotel.dto.request.CreateHotelRequest;
import com.gpsolutions.hotel.dto.response.HotelFullResponse;
import com.gpsolutions.hotel.dto.response.HotelShortResponse;
import com.gpsolutions.hotel.entity.Amenity;
import com.gpsolutions.hotel.entity.Hotel;
import com.gpsolutions.hotel.exception.HotelNotFoundException;
import com.gpsolutions.hotel.mapper.HotelMapper;
import com.gpsolutions.hotel.repository.AmenityRepository;
import com.gpsolutions.hotel.repository.HotelRepository;
import com.gpsolutions.hotel.repository.HotelSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of HotelService.
 * Contains business logic for hotel management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    /**
     * Get all hotels with short information.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> getAllHotels() {
        log.debug("Fetching all hotels");
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toShortResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get hotel by id with full information.
     */
    @Override
    @Transactional(readOnly = true)
    public HotelFullResponse getHotelById(Long id) {
        log.debug("Fetching hotel by id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return hotelMapper.toFullResponse(hotel);
    }

    /**
     * Search hotels by various criteria using JPA Specification.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        log.debug("Searching hotels with criteria - name: {}, brand: {}, city: {}, country: {}, amenities: {}",
                name, brand, city, country, amenities);

        HotelSpecification spec = new HotelSpecification(name, brand, city, country, amenities);
        return hotelRepository.findAll(spec).stream()
                .map(hotelMapper::toShortResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create a new hotel.
     */
    @Override
    public HotelShortResponse createHotel(CreateHotelRequest request) {
        log.debug("Creating new hotel: {}", request.getName());

        Hotel hotel = hotelMapper.toEntity(request);
        Hotel savedHotel = hotelRepository.save(hotel);

        log.info("Hotel created successfully with id: {}", savedHotel.getId());
        return hotelMapper.toShortResponse(savedHotel);
    }

    /**
     * Add amenities to a hotel.
     */
    @Override
    public HotelFullResponse addAmenitiesToHotel(Long hotelId, List<String> amenityNames) {
        log.debug("Adding amenities to hotel id: {}", hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        // Find or create amenities
        Set<Amenity> amenities = new HashSet<>();
        for (String amenityName : amenityNames) {
            Amenity amenity = amenityRepository.findByName(amenityName)
                    .orElseGet(() -> {
                        Amenity newAmenity = Amenity.builder()
                                .name(amenityName)
                                .build();
                        return amenityRepository.save(newAmenity);
                    });
            amenities.add(amenity);
        }

        hotel.setAmenities(amenities);
        Hotel updatedHotel = hotelRepository.save(hotel);

        log.info("Amenities added to hotel id: {}", hotelId);
        return hotelMapper.toFullResponse(updatedHotel);
    }

    /**
     * Get histogram data grouped by parameter.
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        log.debug("Getting histogram for parameter: {}", param);

        List<Object[]> results = switch (param.toLowerCase()) {
            case "brand" -> hotelRepository.countByBrand();
            case "city" -> hotelRepository.countByCity();
            case "country" -> hotelRepository.countByCountry();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Invalid histogram parameter: " + param +
                    ". Must be one of: brand, city, country, amenities");
        };

        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).longValue()
                ));
    }
}
