package com.example.order_mgnt.service;

import com.example.order_mgnt.dto.request.PaymentRequestDTO;
import com.example.order_mgnt.dto.response.PaymentResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponseDTO create(PaymentRequestDTO dto);
    PaymentResponseDTO getById(UUID id);
    List<PaymentResponseDTO> getAll();
    PaymentResponseDTO update(UUID id, PaymentRequestDTO dto);
    void delete(UUID id);
}
