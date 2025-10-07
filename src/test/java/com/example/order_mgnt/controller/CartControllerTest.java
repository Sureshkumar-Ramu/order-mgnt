package com.example.order_mgnt.controller;

import com.example.order_mgnt.dto.request.CartRequestDTO;
import com.example.order_mgnt.dto.response.CartResponseDTO;
import com.example.order_mgnt.service.CartService;
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

@WebMvcTest(CartController.class)
@Import(CartControllerTest.MockConfig.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID cartId;
    private CartResponseDTO responseDTO;
    private CartRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cartId = UUID.randomUUID();

        requestDTO = new CartRequestDTO();
        // Initialize fields of requestDTO as needed

        responseDTO = new CartResponseDTO();
        responseDTO.setId(cartId);
    }

    @Test
    void testCreateCart() throws Exception {
        Mockito.when(cartService.create(any(CartRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId.toString()));
    }

    @Test
    void testGetCartById() throws Exception {
        Mockito.when(cartService.getById(cartId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/carts/{id}", cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId.toString()));
    }

    @Test
    void testGetAllCarts() throws Exception {
        Mockito.when(cartService.getAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateCart() throws Exception {
        Mockito.when(cartService.update(eq(cartId), any(CartRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/carts/{id}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId.toString()));
    }

    @Test
    void testDeleteCart() throws Exception {
        Mockito.doNothing().when(cartService).delete(cartId);

        mockMvc.perform(delete("/api/carts/{id}", cartId))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CartService cartService() {
            return Mockito.mock(CartService.class);
        }
    }
}


