package com.example.order_mgnt.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartResponseDTO {
    private UUID id;
    private UUID userId;
    private List<CartItemResponseDTO> items;
}

