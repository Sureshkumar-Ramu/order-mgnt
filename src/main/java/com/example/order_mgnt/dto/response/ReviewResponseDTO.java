package com.example.order_mgnt.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewResponseDTO {
    private UUID id;
    private UUID bookId;
    private UUID userId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
}

