package com.example.order_mgnt.repository;

import com.example.order_mgnt.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    // Additional query methods can be defined here if needed
}
