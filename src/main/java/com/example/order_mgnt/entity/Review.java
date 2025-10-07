package com.example.order_mgnt.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.order_mgnt.entity.book_mgnt.*;
@Entity
@Table(name = "review")
@Data
public class Review {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int rating;

    private String comment;

    private LocalDateTime review_date = LocalDateTime.now();
}

