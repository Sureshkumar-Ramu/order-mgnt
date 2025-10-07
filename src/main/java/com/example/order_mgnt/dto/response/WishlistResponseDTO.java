package com.example.order_mgnt.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class WishlistResponseDTO {
    private UUID id;
    private UUID userId;
    private UUID bookId;
}

