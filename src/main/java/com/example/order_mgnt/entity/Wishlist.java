package com.example.order_mgnt.entity;


import com.example.order_mgnt.entity.book_mgnt.Book;
import com.example.order_mgnt.entity.book_mgnt.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wishlist")
@Data
public class Wishlist {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Transient
    private LocalDateTime addedAt = LocalDateTime.now();
}

