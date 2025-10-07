package com.example.order_mgnt.service;

import com.example.order_mgnt.dto.request.ReviewRequestDTO;
import com.example.order_mgnt.dto.response.ReviewResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewResponseDTO create(ReviewRequestDTO dto);
    ReviewResponseDTO getById(UUID id);
    List<ReviewResponseDTO> getAll();
    ReviewResponseDTO update(UUID id, ReviewRequestDTO dto);
    void delete(UUID id);
}

