package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.WishlistRequestDTO;
import com.example.order_mgnt.dto.response.WishlistResponseDTO;
import com.example.order_mgnt.entity.Wishlist;
import com.example.order_mgnt.exception.ResourceNotFoundException;
import com.example.order_mgnt.repository.BookRepository;
import com.example.order_mgnt.repository.UserRepository;
import com.example.order_mgnt.repository.WishlistRepository;
import com.example.order_mgnt.service.WishlistService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public WishlistServiceImpl(UserRepository userRepository,BookRepository bookRepository,WishlistRepository wishlistRepository, ModelMapper modelMapper) {
        this.wishlistRepository = wishlistRepository;
        this.modelMapper = modelMapper;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WishlistResponseDTO create(WishlistRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("WishlistRequest cannot be null");
        }
        try {
            Wishlist wishlist=new Wishlist();
            wishlist.setBook(bookRepository.findById(dto.getBookId()).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBookId())));
            wishlist.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId())));
            return modelMapper.map(wishlistRepository.save(wishlist), WishlistResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creating wishlist: " + e.getMessage());
        }
    }

    @Override
    public WishlistResponseDTO getById(UUID id) {
        return wishlistRepository.findById(id)
                .map(w -> modelMapper.map(w, WishlistResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + id));
    }

    @Override
    public List<WishlistResponseDTO> getAll() {
        return wishlistRepository.findAll().stream()
                .map(w -> modelMapper.map(w, WishlistResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public WishlistResponseDTO update(UUID id, WishlistRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("WishlistRequest cannot be null");
        }
        try {
            Wishlist wishlist = wishlistRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + id));
            wishlist.setBook(bookRepository.findById(dto.getBookId()).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBookId())));
            wishlist.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId())));
            return modelMapper.map(wishlistRepository.save(wishlist), WishlistResponseDTO.class);
        }catch (Exception e) {
            throw new RuntimeException("Error updating wishlist: " + e.getMessage());
        }

    }

    @Override
    public void delete(UUID id) {
        wishlistRepository.deleteById(id);
    }
}
