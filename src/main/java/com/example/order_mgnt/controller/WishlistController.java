package com.example.order_mgnt.controller;

import com.example.order_mgnt.dto.request.WishlistRequestDTO;
import com.example.order_mgnt.dto.response.WishlistResponseDTO;
import com.example.order_mgnt.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponseDTO> create(@RequestBody WishlistRequestDTO dto) {
        return ResponseEntity.ok(wishlistService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(wishlistService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDTO>> getAll() {
        return ResponseEntity.ok(wishlistService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponseDTO> update(@PathVariable UUID id, @RequestBody WishlistRequestDTO dto) {
        return ResponseEntity.ok(wishlistService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        wishlistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
