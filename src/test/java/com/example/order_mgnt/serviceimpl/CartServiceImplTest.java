package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.CartRequestDTO;
import com.example.order_mgnt.dto.response.CartResponseDTO;
import com.example.order_mgnt.entity.Cart;
import com.example.order_mgnt.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CartServiceImplTest {
    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        CartRequestDTO dto = new CartRequestDTO();
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        when(cartRepository.save(any())).thenReturn(cart);

        CartResponseDTO response = cartService.create(dto);

        assertNotNull(response);
    }

    @Test
    void testGetCartById() {
        UUID id = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(id);
        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        CartResponseDTO dto = cartService.getById(id);
        assertNotNull(dto);
    }
}
