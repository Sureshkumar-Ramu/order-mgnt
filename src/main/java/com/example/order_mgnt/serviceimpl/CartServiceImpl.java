package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.CartRequestDTO;
import com.example.order_mgnt.dto.response.CartResponseDTO;
import com.example.order_mgnt.entity.Cart;
import com.example.order_mgnt.entity.CartItem;
import com.example.order_mgnt.entity.book_mgnt.Book;
import com.example.order_mgnt.exception.ResourceNotFoundException;
import com.example.order_mgnt.repository.BookRepository;
import com.example.order_mgnt.repository.CartRepository;
import com.example.order_mgnt.repository.UserRepository;
import com.example.order_mgnt.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public CartResponseDTO create(CartRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CartRequest cannot be null");
        }
        if(dto.getUserId()!=null){
            userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + dto.getUserId()));
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart must have at least one item");
        }

        try{
            Cart cart=new Cart();
            cart.setUser(userRepository.findById(dto.getUserId()).get());
            Cart finalCart = cart;
            Set<CartItem> cartItems = dto.getItems().stream()
                    .map(itemDTO -> {
                        CartItem item = new CartItem();
                        item.setCart(finalCart);
                        item.setBook(bookRepository.findById(itemDTO.getBookId()).get());
                        item.setQuantity(itemDTO.getQuantity());
                        return item;
                    })
                    .collect(Collectors.toSet());
            cart.setItems(cartItems);
            Cart savedCart = cartRepository.save(cart); // Save cascades to items
            return modelMapper.map(savedCart, CartResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating cart: " + e.getMessage(), e);
        }

    }

    @Override
    public CartResponseDTO getById(UUID id) {
        return modelMapper.map(
                cartRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id)),
                CartResponseDTO.class
        );
    }

    @Override
    public List<CartResponseDTO> getAll() {
        return cartRepository.findAll().stream()
                .map(cart -> modelMapper.map(cart, CartResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartResponseDTO update(UUID id, CartRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CartRequest cannot be null");
        }
        try {
            Cart cart = cartRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cart not found with id: " + id));
            cart.setUser(userRepository.findById(dto.getUserId()).get());
            Cart finalCart = cart;
            Set<CartItem> cartItems = dto.getItems().stream()
                    .map(itemDTO -> {
                        CartItem item = new CartItem();
                        item.setCart(finalCart);
                        item.setBook(bookRepository.findById(itemDTO.getBookId()).get());
                        item.setQuantity(itemDTO.getQuantity());
                        return item;
                    })
                    .collect(Collectors.toSet());
            cart.setItems(cartItems);
            return modelMapper.map(cartRepository.save(cart), CartResponseDTO.class);
        }catch (Exception e) {
            throw new RuntimeException("Error updating cart: " + e.getMessage());
        }

    }

    @Override
    public void delete(UUID id) {
        cartRepository.deleteById(id);
    }
}
