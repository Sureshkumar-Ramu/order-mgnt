package com.example.order_mgnt.service;

import com.example.order_mgnt.dto.request.CartRequestDTO;
import com.example.order_mgnt.dto.response.CartResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CartService {
    CartResponseDTO create(CartRequestDTO dto);
    CartResponseDTO getById(UUID id);
    List<CartResponseDTO> getAll();
    CartResponseDTO update(UUID id, CartRequestDTO dto);
    void delete(UUID id);
}
