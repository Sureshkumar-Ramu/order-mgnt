package com.example.order_mgnt.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemResponseDTO {
    private UUID id;
    private UUID bookId;
    private int quantity;
    private BigDecimal price;
}

