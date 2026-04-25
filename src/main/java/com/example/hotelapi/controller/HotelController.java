package com.example.hotelapi.controller;

import com.example.hotelapi.dto.request.HotelCreateRequest;
import com.example.hotelapi.dto.response.HotelDetailResponse;
import com.example.hotelapi.dto.response.HotelShortResponse;
import com.example.hotelapi.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/property-view")
@Tag(name = "Hotel API", description = "Operations for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels")
    public List<HotelShortResponse> getAllHotels() {
        return hotelService.getAll();
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel details by id")
    public HotelDetailResponse getHotelById(@PathVariable Long id) {
        return hotelService.getById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels using filters")
    public List<HotelShortResponse> searchHotels(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String brand,
                                                 @RequestParam(required = false) String city,
                                                 @RequestParam(required = false) String country,
                                                 @RequestParam(required = false)
                                                 @Parameter(description = "Repeat parameter for multiple values, e.g. amenities=Free WiFi&amenities=Spa")
                                                 List<String> amenities) {
        return hotelService.search(name, brand, city, country, amenities);
    }

    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new hotel")
    public HotelShortResponse createHotel(@Valid @RequestBody HotelCreateRequest request) {
        return hotelService.create(request);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to a hotel")
    public HotelDetailResponse addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        return hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram by brand, city, country or amenities")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
