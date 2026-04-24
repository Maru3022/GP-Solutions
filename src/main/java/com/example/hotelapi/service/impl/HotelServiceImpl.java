package com.example.hotelapi.service.impl;

import com.example.hotelapi.dto.request.HotelCreateRequest;
import com.example.hotelapi.dto.response.HotelDetailResponse;
import com.example.hotelapi.dto.response.HotelShortResponse;
import com.example.hotelapi.entity.Amenity;
import com.example.hotelapi.entity.Hotel;
import com.example.hotelapi.exception.HotelNotFoundException;
import com.example.hotelapi.exception.InvalidHistogramParameterException;
import com.example.hotelapi.mapper.HotelMapper;
import com.example.hotelapi.repository.AmenityRepository;
import com.example.hotelapi.repository.HotelRepository;
import com.example.hotelapi.repository.HotelSpecifications;
import com.example.hotelapi.service.HotelService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    public HotelServiceImpl(HotelRepository hotelRepository,
                            AmenityRepository amenityRepository,
                            HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
        this.hotelMapper = hotelMapper;
    }

    @Override
    public List<HotelShortResponse> getAll() {
        return hotelRepository.findAll().stream()
                .sorted(Comparator.comparing(Hotel::getId))
                .map(hotelMapper::toShortResponse)
                .toList();
    }

    @Override
    public HotelDetailResponse getById(Long id) {
        return hotelMapper.toDetailResponse(getHotelOrThrow(id));
    }

    @Override
    public List<HotelShortResponse> search(String name, String brand, String city, String country, List<String> amenities) {
        Specification<Hotel> specification = Specification.allOf(
                HotelSpecifications.nameContains(name),
                HotelSpecifications.brandEquals(brand),
                HotelSpecifications.cityEquals(city),
                HotelSpecifications.countryEquals(country),
                HotelSpecifications.hasAmenities(amenities)
        );

        return hotelRepository.findAll(specification).stream()
                .sorted(Comparator.comparing(Hotel::getId))
                .map(hotelMapper::toShortResponse)
                .toList();
    }

    @Override
    @Transactional
    public HotelShortResponse create(HotelCreateRequest request) {
        Hotel savedHotel = hotelRepository.save(hotelMapper.toEntity(request));
        return hotelMapper.toShortResponse(savedHotel);
    }

    @Override
    @Transactional
    public HotelDetailResponse addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = getHotelOrThrow(hotelId);
        if (amenities != null) {
            amenities.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .map(this::getOrCreateAmenity)
                    .forEach(hotel.getAmenities()::add);
        }
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toDetailResponse(savedHotel);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        String normalizedParam = normalize(param);
        return switch (normalizedParam) {
            case "brand" -> toHistogramMap(hotelRepository.countByBrand());
            case "city" -> toHistogramMap(hotelRepository.countByCity());
            case "country" -> toHistogramMap(hotelRepository.countByCountry());
            case "amenities" -> toHistogramMap(amenityRepository.countHotelsByAmenity());
            default -> throw new InvalidHistogramParameterException(param);
        };
    }

    private Hotel getHotelOrThrow(Long id) {
        return hotelRepository.findWithAmenitiesById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
    }

    private Amenity getOrCreateAmenity(String name) {
        return amenityRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> {
                    Amenity amenity = new Amenity();
                    amenity.setName(name);
                    return amenityRepository.save(amenity);
                });
    }

    private Map<String, Long> toHistogramMap(List<Object[]> rows) {
        Map<String, Long> histogram = new LinkedHashMap<>();
        rows.stream()
                .sorted((left, right) -> String.valueOf(left[0]).compareToIgnoreCase(String.valueOf(right[0])))
                .forEach(row -> histogram.put(String.valueOf(row[0]), ((Number) row[1]).longValue()));
        return histogram;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
