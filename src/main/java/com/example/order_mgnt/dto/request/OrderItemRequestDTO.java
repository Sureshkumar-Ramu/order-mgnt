package com.example.order_mgnt.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemRequestDTO {
    private UUID bookId;
    private int quantity;
    private BigDecimal price;
}

