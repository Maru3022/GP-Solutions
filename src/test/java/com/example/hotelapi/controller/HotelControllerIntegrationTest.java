package com.example.hotelapi.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllHotels() throws Exception {
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void shouldFilterHotelsByCity() throws Exception {
        mockMvc.perform(get("/property-view/search").param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("DoubleTree by Hilton Minsk")));
    }

    @Test
    void shouldCreateHotel() throws Exception {
        String payload = """
                {
                  "name": "Test Hotel",
                  "description": "Test description",
                  "brand": "Test Brand",
                  "address": {
                    "houseNumber": "10",
                    "street": "Lenina Street",
                    "city": "Brest",
                    "country": "Belarus",
                    "postCode": "224000"
                  },
                  "contacts": {
                    "phone": "+375 29 111-11-11",
                    "email": "test.hotel@example.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """;

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.address").value("10 Lenina Street, Brest, 224000, Belarus"));
    }

    @Test
    void shouldReturnHistogramByCity() throws Exception {
        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(2));
    }

    @Test
    void shouldExposeSwaggerApiDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Hotel API"));
    }

    @Test
    void shouldReturnHotelDetails() throws Exception {
        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.amenities", hasItem("Free WiFi")));
    }

    @Test
    void shouldReturnNotFoundForMissingHotel() throws Exception {
        mockMvc.perform(get("/property-view/hotels/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    @Test
    void shouldReturnBadRequestForInvalidHistogramParameter() throws Exception {
        mockMvc.perform(get("/property-view/histogram/unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Allowed values")));
    }

    @Test
    void shouldAddAmenitiesToHotel() throws Exception {
        String payload = """
                [
                  "Spa",
                  "Airport transfer"
                ]
                """;

        mockMvc.perform(post("/property-view/hotels/2/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities", hasItem("Spa")))
                .andExpect(jsonPath("$.amenities", hasItem("Airport transfer")));
    }

    @Test
    void shouldReturnConflictForDuplicateEmail() throws Exception {
        String payload = """
                {
                  "name": "Duplicate Email Hotel",
                  "description": "Duplicate email test",
                  "brand": "Brand",
                  "address": {
                    "houseNumber": "99",
                    "street": "Test Street",
                    "city": "Grodno",
                    "country": "Belarus",
                    "postCode": "230000"
                  },
                  "contacts": {
                    "phone": "+375 29 999-99-99",
                    "email": "doubletreeminsk.info@hilton.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """;

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Hotel with the same email already exists"));
    }
}
