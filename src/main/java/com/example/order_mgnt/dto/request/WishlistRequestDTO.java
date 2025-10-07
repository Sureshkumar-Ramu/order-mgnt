package com.example.order_mgnt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WishlistRequestDTO {
    @NotNull(message = "User ID cannot be null")
    private UUID userId;
    @NotNull(message = "Book ID cannot be null")
    private UUID bookId;
}

