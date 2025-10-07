package com.example.order_mgnt.serviceimpl;


import com.example.order_mgnt.dto.request.WishlistRequestDTO;
import com.example.order_mgnt.dto.response.WishlistResponseDTO;
import com.example.order_mgnt.entity.Wishlist;
import com.example.order_mgnt.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceImplTest {

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ModelMapper modelMapper;

    private UUID wishlistId;
    private Wishlist wishlist;
    private WishlistRequestDTO requestDTO;
    private WishlistResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        wishlistId = UUID.randomUUID();

        wishlist = new Wishlist();
        wishlist.setId(wishlistId);

        requestDTO = new WishlistRequestDTO();
        responseDTO = new WishlistResponseDTO();
        responseDTO.setId(wishlistId);
    }

    @Test
    void testCreateWishlist() {
        when(modelMapper.map(requestDTO, Wishlist.class)).thenReturn(wishlist);
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);
        when(modelMapper.map(wishlist, WishlistResponseDTO.class)).thenReturn(responseDTO);

        WishlistResponseDTO result = wishlistService.create(requestDTO);
        assertNotNull(result);
        assertEquals(wishlistId, result.getId());
    }

    @Test
    void testGetWishlistById() {
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(modelMapper.map(wishlist, WishlistResponseDTO.class)).thenReturn(responseDTO);

        WishlistResponseDTO result = wishlistService.getById(wishlistId);
        assertNotNull(result);
        assertEquals(wishlistId, result.getId());
    }

    @Test
    void testGetAllWishlists() {
        when(wishlistRepository.findAll()).thenReturn(List.of(wishlist));
        when(modelMapper.map(wishlist, WishlistResponseDTO.class)).thenReturn(responseDTO);

        List<WishlistResponseDTO> result = wishlistService.getAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void testUpdateWishlist() {
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(wishlist)).thenReturn(wishlist);
        when(modelMapper.map(wishlist, WishlistResponseDTO.class)).thenReturn(responseDTO);

        WishlistResponseDTO result = wishlistService.update(wishlistId, requestDTO);
        assertNotNull(result);
        assertEquals(wishlistId, result.getId());
    }

    @Test
    void testDeleteWishlist() {
        when(wishlistRepository.existsById(wishlistId)).thenReturn(true);
        doNothing().when(wishlistRepository).deleteById(wishlistId);

        assertDoesNotThrow(() -> wishlistService.delete(wishlistId));
        verify(wishlistRepository, times(1)).deleteById(wishlistId);
    }
}

