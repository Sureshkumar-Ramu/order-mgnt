package com.example.order_mgnt.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequestDTO {

    private UUID userId;
    private List<OrderItemRequestDTO> items;
    private BigDecimal totalAmount;
    private String status;
}
