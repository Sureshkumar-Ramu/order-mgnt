package com.example.order_mgnt.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemResponseDTO {
    private UUID id;
    private UUID bookId;
    private int quantity;
}
