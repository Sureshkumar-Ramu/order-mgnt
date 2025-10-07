package com.example.order_mgnt.controller;

import com.example.order_mgnt.dto.request.CartRequestDTO;
import com.example.order_mgnt.dto.response.CartResponseDTO;
import com.example.order_mgnt.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDTO> create(@RequestBody CartRequestDTO dto) {
        return ResponseEntity.ok(cartService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(cartService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getAll() {
        return ResponseEntity.ok(cartService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponseDTO> update(@PathVariable UUID id, @RequestBody CartRequestDTO dto) {
        return ResponseEntity.ok(cartService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
