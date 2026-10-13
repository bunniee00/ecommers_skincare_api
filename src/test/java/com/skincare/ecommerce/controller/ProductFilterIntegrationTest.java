package com.skincare.ecommerce.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/products/filter - Returns seeded products without filter")
    void testFilterWithoutParams() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(10))))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(10)))
                .andExpect(jsonPath("$.pageNumber", is(0)));
    }

    @Test
    @DisplayName("GET /api/products/filter - Filter by keyword matches name or brand")
    void testFilterByKeyword() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("keyword", "cerave")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].brand", is("CeraVe")));
    }

    @Test
    @DisplayName("GET /api/products/filter - Filter by skinType")
    void testFilterBySkinType() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("skinType", "OILY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].skinType", is("OILY")));
    }

    @Test
    @DisplayName("GET /api/products/filter - Filter by price range and sort ascending")
    void testFilterByPriceRangeAndSort() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("minPrice", "20.00")
                        .param("maxPrice", "40.00")
                        .param("sortBy", "price")
                        .param("sortDir", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].price", greaterThanOrEqualTo(20.0)))
                .andExpect(jsonPath("$.content[0].price", lessThanOrEqualTo(40.0)));
    }

    @Test
    @DisplayName("GET /api/products/filter - Filter by isBestSeller flag")
    void testFilterByBestSeller() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("isBestSeller", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[*].isBestSeller", everyItem(is(true))));
    }

    @Test
    @DisplayName("GET /api/products/filter - Filter by inStock true")
    void testFilterByInStock() throws Exception {
        mockMvc.perform(get("/api/products/filter")
                        .param("inStock", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[*].stock", everyItem(greaterThan(0))));
    }
}
