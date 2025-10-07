package com.example.order_mgnt.controller;


import com.example.order_mgnt.dto.request.WishlistRequestDTO;
import com.example.order_mgnt.dto.response.WishlistResponseDTO;
import com.example.order_mgnt.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
@Import(WishlistControllerTest.MockConfig.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID wishlistId;
    private WishlistRequestDTO requestDTO;
    private WishlistResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        wishlistId = UUID.randomUUID();

        requestDTO = new WishlistRequestDTO();
        // Populate with test data

        responseDTO = new WishlistResponseDTO();
        responseDTO.setId(wishlistId);
        // Populate with test data
    }

    @Test
    void testCreateWishlist() throws Exception {
        Mockito.when(wishlistService.create(any(WishlistRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/wishlists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wishlistId.toString()));
    }

    @Test
    void testGetWishlistById() throws Exception {
        Mockito.when(wishlistService.getById(wishlistId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/wishlists/{id}", wishlistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wishlistId.toString()));
    }

    @Test
    void testGetAllWishlists() throws Exception {
        Mockito.when(wishlistService.getAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/wishlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateWishlist() throws Exception {
        Mockito.when(wishlistService.update(eq(wishlistId), any(WishlistRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/wishlists/{id}", wishlistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wishlistId.toString()));
    }

    @Test
    void testDeleteWishlist() throws Exception {
        Mockito.doNothing().when(wishlistService).delete(wishlistId);

        mockMvc.perform(delete("/api/wishlists/{id}", wishlistId))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public WishlistService wishlistService() {
            return Mockito.mock(WishlistService.class);
        }
    }
}

