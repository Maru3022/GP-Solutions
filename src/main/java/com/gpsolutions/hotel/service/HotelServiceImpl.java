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
import java.util.Locale;
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
        log.debug("Fetching all hotels from database");
        List<HotelShortResponse> hotels = hotelRepository.findAll().stream()
                .map(hotelMapper::toShortResponse)
                .collect(Collectors.toList());
        log.info("Successfully fetched {} hotels", hotels.size());
        return hotels;
    }

    /**
     * Get hotel by id with full information.
     */
    @Override
    @Transactional(readOnly = true)
    public HotelFullResponse getHotelById(Long id) {
        log.debug("Fetching hotel by ID: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Hotel not found with ID: {}", id);
                    return new HotelNotFoundException(id);
                });
        log.info("Successfully fetched hotel: {} (ID: {})", hotel.getName(), id);
        return hotelMapper.toFullResponse(hotel);
    }

    /**
     * Search hotels by various criteria using JPA Specification.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        log.debug("Searching hotels with criteria - name: {}, brand: {}, city: {}, country: {}, amenities count: {}",
                name, brand, city, country, amenities != null ? amenities.size() : 0);

        HotelSpecification spec = new HotelSpecification(name, brand, city, country, amenities);
        List<HotelShortResponse> results = hotelRepository.findAll(spec).stream()
                .map(hotelMapper::toShortResponse)
                .collect(Collectors.toList());
        
        log.info("Search completed: found {} hotels matching criteria", results.size());
        return results;
    }

    /**
     * Create a new hotel.
     */
    @Override
    public HotelShortResponse createHotel(CreateHotelRequest request) {
        log.info("Creating new hotel: {}", request.getName());
        log.debug("Hotel creation details - Brand: {}, City: {}, Country: {}", 
                request.getBrand(), 
                request.getAddress() != null ? request.getAddress().getCity() : "N/A",
                request.getAddress() != null ? request.getAddress().getCountry() : "N/A");

        Hotel hotel = hotelMapper.toEntity(request);
        Hotel savedHotel = hotelRepository.save(hotel);

        log.info("Hotel created successfully with ID: {}", savedHotel.getId());
        return hotelMapper.toShortResponse(savedHotel);
    }

    /**
     * Add amenities to a hotel.
     */
    @Override
    public HotelFullResponse addAmenitiesToHotel(Long hotelId, List<String> amenityNames) {
        log.info("Adding {} amenities to hotel ID: {}", amenityNames.size(), hotelId);
        log.debug("Amenities to add: {}", amenityNames);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    log.error("Hotel not found with ID: {}", hotelId);
                    return new HotelNotFoundException(hotelId);
                });

        // Find or create amenities
        Set<Amenity> amenities = new HashSet<>();
        int newAmenitiesCount = 0;
        for (String amenityName : amenityNames) {
            Amenity amenity = amenityRepository.findByName(amenityName)
                    .orElseGet(() -> {
                        log.debug("Creating new amenity: {}", amenityName);
                        Amenity newAmenity = Amenity.builder()
                                .name(amenityName)
                                .build();
                        newAmenitiesCount++;
                        return amenityRepository.save(newAmenity);
                    });
            amenities.add(amenity);
        }

        hotel.setAmenities(amenities);
        Hotel updatedHotel = hotelRepository.save(hotel);

        log.info("Successfully added amenities to hotel ID: {} ({} new amenities created)", hotelId, newAmenitiesCount);
        return hotelMapper.toFullResponse(updatedHotel);
    }

    /**
     * Get histogram data grouped by parameter.
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        log.debug("Getting histogram for parameter: {}", param);

        List<Object[]> results = switch (param.toLowerCase(Locale.ROOT)) {
            case "brand" -> {
                log.debug("Fetching histogram by brand");
                yield hotelRepository.countByBrand();
            }
            case "city" -> {
                log.debug("Fetching histogram by city");
                yield hotelRepository.countByCity();
            }
            case "country" -> {
                log.debug("Fetching histogram by country");
                yield hotelRepository.countByCountry();
            }
            case "amenities" -> {
                log.debug("Fetching histogram by amenities");
                yield hotelRepository.countByAmenities();
            }
            default -> {
                log.error("Invalid histogram parameter: {}", param);
                throw new IllegalArgumentException("Invalid histogram parameter: " + param +
                        ". Must be one of: brand, city, country, amenities");
            }
        };

        Map<String, Long> histogram = results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).longValue()
                ));
        
        log.info("Histogram for '{}' returned {} entries", param, histogram.size());
        return histogram;
    }
}
