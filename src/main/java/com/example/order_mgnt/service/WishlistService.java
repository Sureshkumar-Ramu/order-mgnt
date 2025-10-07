package com.example.order_mgnt.service;

import com.example.order_mgnt.dto.request.WishlistRequestDTO;
import com.example.order_mgnt.dto.response.WishlistResponseDTO;

import java.util.List;
import java.util.UUID;

public interface WishlistService {
    WishlistResponseDTO create(WishlistRequestDTO dto);
    WishlistResponseDTO getById(UUID id);
    List<WishlistResponseDTO> getAll();
    WishlistResponseDTO update(UUID id, WishlistRequestDTO dto);
    void delete(UUID id);
}
