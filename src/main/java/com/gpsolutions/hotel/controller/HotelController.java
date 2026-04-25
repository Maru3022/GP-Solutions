package com.gpsolutions.hotel.controller;

import com.gpsolutions.hotel.dto.request.CreateHotelRequest;
import com.gpsolutions.hotel.dto.response.HotelFullResponse;
import com.gpsolutions.hotel.dto.response.HotelShortResponse;
import com.gpsolutions.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for hotel management.
 * All endpoints are prefixed with /property-view
 */
@Slf4j
@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotel management endpoints")
public class HotelController {

    private final HotelService hotelService;

    /**
     * GET /property-view/hotels
     * Get all hotels with short information.
     *
     * @return List of HotelShortResponse
     */
    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Returns a list of all hotels with short information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotels",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<HotelShortResponse>> getAllHotels() {
        log.info("REST request to get all hotels");
        List<HotelShortResponse> hotels = hotelService.getAllHotels();
        log.debug("Returning {} hotels", hotels.size());
        return ResponseEntity.ok(hotels);
    }

    /**
     * GET /property-view/hotels/{id}
     * Get hotel by id with full information.
     *
     * @param id Hotel id
     * @return HotelFullResponse
     */
    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by id", description = "Returns full information about a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<HotelFullResponse> getHotelById(
            @PathVariable
            @Parameter(description = "Hotel id", required = true)
            Long id) {
        log.info("REST request to get hotel by ID: {}", id);
        HotelFullResponse hotel = hotelService.getHotelById(id);
        log.debug("Returning hotel: {}", hotel.getName());
        return ResponseEntity.ok(hotel);
    }

    /**
     * GET /property-view/search
     * Search hotels by various criteria.
     * Parameters are optional and can be combined.
     *
     * @param name Hotel name (contains search)
     * @param brand Hotel brand
     * @param city City
     * @param country Country
     * @param amenities Comma-separated list of amenities
     * @return List of HotelShortResponse
     */
    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Search hotels by name, brand, city, country, and/or amenities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<HotelShortResponse>> searchHotels(
            @RequestParam(required = false)
            @Parameter(description = "Hotel name (contains search)")
            String name,
            @RequestParam(required = false)
            @Parameter(description = "Hotel brand")
            String brand,
            @RequestParam(required = false)
            @Parameter(description = "City")
            String city,
            @RequestParam(required = false)
            @Parameter(description = "Country")
            String country,
            @RequestParam(required = false)
            @Parameter(description = "Comma-separated amenity names")
            String amenities) {
        log.info("REST request to search hotels - name: {}, brand: {}, city: {}, country: {}, amenities: {}",
                name, brand, city, country, amenities);

        List<String> amenitiesList = amenities != null && !amenities.isEmpty()
                ? List.of(amenities.split(","))
                : null;

        List<HotelShortResponse> results = hotelService.searchHotels(name, brand, city, country, amenitiesList);
        log.debug("Search returned {} hotels", results.size());
        return ResponseEntity.ok(results);
    }

    /**
     * POST /property-view/hotels
     * Create a new hotel.
     *
     * @param request CreateHotelRequest with hotel data
     * @return HotelShortResponse of created hotel
     */
    @PostMapping("/hotels")
    @Operation(summary = "Create hotel", description = "Create a new hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel created successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<HotelShortResponse> createHotel(
            @Valid @RequestBody CreateHotelRequest request) {
        log.info("REST request to create hotel: {}", request.getName());
        log.debug("Hotel details: brand={}, city={}", 
                request.getBrand(), 
                request.getAddress() != null ? request.getAddress().getCity() : "N/A");
        HotelShortResponse createdHotel = hotelService.createHotel(request);
        log.info("Hotel created successfully with ID: {}", createdHotel.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    /**
     * POST /property-view/hotels/{id}/amenities
     * Add amenities to a hotel.
     *
     * @param id Hotel id
     * @param amenities JSON array of amenity names
     * @return HotelFullResponse with updated amenities
     */
    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to hotel", description = "Add one or more amenities to a hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amenities added successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<HotelFullResponse> addAmenitiesToHotel(
            @PathVariable
            @Parameter(description = "Hotel id", required = true)
            Long id,
            @Valid @RequestBody List<String> amenities) {
        log.info("REST request to add amenities to hotel ID: {}", id);
        log.debug("Adding {} amenities: {}", amenities.size(), amenities);
        HotelFullResponse updatedHotel = hotelService.addAmenitiesToHotel(id, amenities);
        log.info("Successfully added amenities to hotel ID: {}", id);
        return ResponseEntity.ok(updatedHotel);
    }

    /**
     * GET /property-view/histogram/{param}
     * Get histogram data grouped by parameter.
     * Parameters can be: brand, city, country, amenities
     *
     * @param param Grouping parameter
     * @return Map with parameter values and counts
     */
    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram", description = "Get count of hotels grouped by parameter (brand, city, country, amenities)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histogram data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid parameter",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Map<String, Long>> getHistogram(
            @PathVariable
            @Parameter(description = "Grouping parameter: brand, city, country, or amenities", required = true)
            String param) {
        log.info("REST request to get histogram by parameter: {}", param);
        Map<String, Long> histogram = hotelService.getHistogram(param);
        log.debug("Histogram returned with {} entries", histogram.size());
        return ResponseEntity.ok(histogram);
    }
}
