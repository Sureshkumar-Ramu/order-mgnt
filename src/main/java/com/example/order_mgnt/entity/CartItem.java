package com.example.order_mgnt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
import com.example.order_mgnt.entity.book_mgnt.*;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int quantity;
}

