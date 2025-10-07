package com.example.order_mgnt.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequestDTO {
    private UUID orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
}

