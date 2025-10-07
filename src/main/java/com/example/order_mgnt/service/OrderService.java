package com.example.order_mgnt.service;

import com.example.order_mgnt.dto.request.OrderRequestDTO;
import com.example.order_mgnt.dto.response.OrderResponseDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponseDTO create(OrderRequestDTO dto);
    OrderResponseDTO getById(UUID id);
    List<OrderResponseDTO> getAll();
    OrderResponseDTO update(UUID id, OrderRequestDTO dto);
    void delete(UUID id);
    OrderResponseDTO convertWishlistToOrder(List<UUID> wishlistIds);
    OrderResponseDTO convertCartToOrder(UUID cartId);
}
