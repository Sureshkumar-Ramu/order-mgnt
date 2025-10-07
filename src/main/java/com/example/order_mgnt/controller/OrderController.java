package com.example.order_mgnt.controller;

import com.example.order_mgnt.dto.request.OrderRequestDTO;
import com.example.order_mgnt.dto.response.OrderResponseDTO;
import com.example.order_mgnt.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(@PathVariable UUID id, @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wishlist-to-order/{wishlistId}")
    public ResponseEntity<OrderResponseDTO> convertWishlistToOrder(@PathVariable List<UUID> wishlistIds) {
        OrderResponseDTO response = orderService.convertWishlistToOrder(wishlistIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cart-to-order/{cartId}")
    public ResponseEntity<OrderResponseDTO> convertCartToOrder(@PathVariable UUID cartId) {
        OrderResponseDTO response = orderService.convertCartToOrder(cartId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
