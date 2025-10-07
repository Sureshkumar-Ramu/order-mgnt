package com.example.order_mgnt.controller;


import com.example.order_mgnt.dto.request.PaymentRequestDTO;
import com.example.order_mgnt.dto.response.PaymentResponseDTO;
import com.example.order_mgnt.service.PaymentService;
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

@WebMvcTest(PaymentController.class)
@Import(PaymentControllerTest.MockConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID paymentId;
    private PaymentRequestDTO requestDTO;
    private PaymentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        paymentId = UUID.randomUUID();

        requestDTO = new PaymentRequestDTO();
        // Set fields as needed

        responseDTO = new PaymentResponseDTO();
        responseDTO.setId(paymentId);
    }

    @Test
    void testCreatePayment() throws Exception {
        Mockito.when(paymentService.create(any(PaymentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentId.toString()));
    }

    @Test
    void testGetPaymentById() throws Exception {
        Mockito.when(paymentService.getById(paymentId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/payments/{id}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentId.toString()));
    }

    @Test
    void testGetAllPayments() throws Exception {
        Mockito.when(paymentService.getAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdatePayment() throws Exception {
        Mockito.when(paymentService.update(eq(paymentId), any(PaymentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/payments/{id}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentId.toString()));
    }

    @Test
    void testDeletePayment() throws Exception {
        Mockito.doNothing().when(paymentService).delete(paymentId);

        mockMvc.perform(delete("/api/payments/{id}", paymentId))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }
    }
}

