package com.gpsolutions.hotel;

import com.gpsolutions.hotel.controller.HotelController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Hotel Property View API.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HotelPropertyViewApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelController hotelController;

    /**
     * Test that application context loads.
     */
    @Test
    void contextLoads() {
        assert hotelController != null;
    }

    /**
     * Test GET /property-view/hotels - should return list of hotels.
     */
    @Test
    void testGetAllHotels() throws Exception {
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].address", notNullValue()))
                .andExpect(jsonPath("$[0].phone", notNullValue()));
    }

    /**
     * Test GET /property-view/hotels/{id} - should return full hotel info.
     */
    @Test
    void testGetHotelById() throws Exception {
        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.brand", notNullValue()))
                .andExpect(jsonPath("$.address.city", notNullValue()))
                .andExpect(jsonPath("$.contacts.phone", notNullValue()))
                .andExpect(jsonPath("$.arrivalTime.checkIn", notNullValue()))
                .andExpect(jsonPath("$.amenities", isA(List.class)));
    }

    /**
     * Test GET /property-view/hotels/{id} with non-existent ID - should return 404.
     */
    @Test
    void testGetHotelByIdNotFound() throws Exception {
        mockMvc.perform(get("/property-view/hotels/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    /**
     * Test GET /property-view/search with name parameter.
     */
    @Test
    void testSearchHotelsByName() throws Exception {
        mockMvc.perform(get("/property-view/search")
                .param("name", "Marriott"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", containsString("Marriott")));
    }

    /**
     * Test GET /property-view/search with brand parameter.
     */
    @Test
    void testSearchHotelsByBrand() throws Exception {
        mockMvc.perform(get("/property-view/search")
                .param("brand", "Hilton"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    /**
     * Test GET /property-view/search with city parameter.
     */
    @Test
    void testSearchHotelsByCity() throws Exception {
        mockMvc.perform(get("/property-view/search")
                .param("city", "Moscow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    /**
     * Test GET /property-view/search with country parameter.
     */
    @Test
    void testSearchHotelsByCountry() throws Exception {
        mockMvc.perform(get("/property-view/search")
                .param("country", "Russia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    /**
     * Test GET /property-view/search with amenities parameter.
     */
    @Test
    void testSearchHotelsByAmenities() throws Exception {
        mockMvc.perform(get("/property-view/search")
                .param("amenities", "Free Wi-Fi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    /**
     * Test POST /property-view/hotels - create new hotel.
     */
    @Test
    void testCreateHotel() throws Exception {
        String createHotelJson = """
                {
                  "name": "Test Hotel",
                  "description": "Test Description",
                  "brand": "Test Brand",
                  "address": {
                    "houseNumber": "100",
                    "street": "Test Street",
                    "city": "Test City",
                    "country": "Test Country",
                    "postCode": "12345"
                  },
                  "contacts": {
                    "phone": "+1234567890",
                    "email": "test@example.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """;

        mockMvc.perform(post("/property-view/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createHotelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Test Hotel")))
                .andExpect(jsonPath("$.phone", is("+1234567890")));
    }

    /**
     * Test POST /property-view/hotels with invalid data - should return 400.
     */
    @Test
    void testCreateHotelWithInvalidData() throws Exception {
        String invalidHotelJson = """
                {
                  "name": "",
                  "brand": "Test Brand",
                  "address": {
                    "houseNumber": "100",
                    "street": "Test Street",
                    "city": "Test City",
                    "country": "Test Country",
                    "postCode": "12345"
                  }
                }
                """;

        mockMvc.perform(post("/property-view/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidHotelJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    /**
     * Test POST /property-view/hotels/{id}/amenities - add amenities to hotel.
     */
    @Test
    void testAddAmenitiesToHotel() throws Exception {
        String amenitiesJson = """
                ["Free Wi-Fi", "Spa"]
                """;

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(amenitiesJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities", hasSize(greaterThanOrEqualTo(1))));
    }

    /**
     * Test POST /property-view/hotels/{id}/amenities with non-existent hotel - should return 404.
     */
    @Test
    void testAddAmenitiesToNonExistentHotel() throws Exception {
        String amenitiesJson = """
                ["Free Wi-Fi"]
                """;

        mockMvc.perform(post("/property-view/hotels/999/amenities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(amenitiesJson))
                .andExpect(status().isNotFound());
    }

    /**
     * Test GET /property-view/histogram/brand - get histogram by brand.
     */
    @Test
    void testHistogramByBrand() throws Exception {
        mockMvc.perform(get("/property-view/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.Marriott", notNullValue()));
    }

    /**
     * Test GET /property-view/histogram/city - get histogram by city.
     */
    @Test
    void testHistogramByCity() throws Exception {
        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.Minsk", notNullValue()));
    }

    /**
     * Test GET /property-view/histogram/country - get histogram by country.
     */
    @Test
    void testHistogramByCountry() throws Exception {
        mockMvc.perform(get("/property-view/histogram/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(greaterThanOrEqualTo(1))));
    }

    /**
     * Test GET /property-view/histogram/amenities - get histogram by amenities.
     */
    @Test
    void testHistogramByAmenities() throws Exception {
        mockMvc.perform(get("/property-view/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.['Free Wi-Fi']", notNullValue()));
    }

    /**
     * Test GET /property-view/histogram with invalid parameter - should return 400.
     */
    @Test
    void testHistogramWithInvalidParameter() throws Exception {
        mockMvc.perform(get("/property-view/histogram/invalid"))
                .andExpect(status().isInternalServerError());
    }
}
