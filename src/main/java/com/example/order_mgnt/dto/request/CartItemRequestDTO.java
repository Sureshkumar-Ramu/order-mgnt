package com.example.order_mgnt.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemRequestDTO {
    private UUID bookId;
    private int quantity;
}

