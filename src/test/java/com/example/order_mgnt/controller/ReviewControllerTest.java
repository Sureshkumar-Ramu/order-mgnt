package com.example.order_mgnt.controller;

import com.example.order_mgnt.dto.request.ReviewRequestDTO;
import com.example.order_mgnt.dto.response.ReviewResponseDTO;
import com.example.order_mgnt.service.ReviewService;
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

@WebMvcTest(ReviewController.class)
@Import(ReviewControllerTest.MockConfig.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID reviewId;
    private ReviewRequestDTO requestDTO;
    private ReviewResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        reviewId = UUID.randomUUID();

        requestDTO = new ReviewRequestDTO();
        // Populate requestDTO with necessary data

        responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
        // Populate responseDTO with other necessary data
    }

    @Test
    void testCreateReview() throws Exception {
        Mockito.when(reviewService.create(any(ReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId.toString()));
    }

    @Test
    void testGetReviewById() throws Exception {
        Mockito.when(reviewService.getById(reviewId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/reviews/{id}", reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId.toString()));
    }

    @Test
    void testGetAllReviews() throws Exception {
        Mockito.when(reviewService.getAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateReview() throws Exception {
        Mockito.when(reviewService.update(eq(reviewId), any(ReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/reviews/{id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId.toString()));
    }

    @Test
    void testDeleteReview() throws Exception {
        Mockito.doNothing().when(reviewService).delete(reviewId);

        mockMvc.perform(delete("/api/reviews/{id}", reviewId))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ReviewService reviewService() {
            return Mockito.mock(ReviewService.class);
        }
    }
}
