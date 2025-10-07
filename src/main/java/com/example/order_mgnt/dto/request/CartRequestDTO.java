package com.example.order_mgnt.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartRequestDTO {
    private UUID userId;
    private List<CartItemRequestDTO> items;
}

