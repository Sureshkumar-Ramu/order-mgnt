package com.example.order_mgnt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequestDTO {

    @NotNull(message = "Book ID cannot be null")
    private UUID bookId;
    @NotNull(message = "User ID cannot be null")
    private UUID userId;
    private int rating;
    private String comment;
}

