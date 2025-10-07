package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.OrderItemRequestDTO;
import com.example.order_mgnt.dto.request.OrderRequestDTO;
import com.example.order_mgnt.dto.response.OrderResponseDTO;
import com.example.order_mgnt.entity.*;
import com.example.order_mgnt.entity.book_mgnt.Book;
import com.example.order_mgnt.entity.book_mgnt.User;
import com.example.order_mgnt.exception.ResourceNotFoundException;
import com.example.order_mgnt.repository.*;
import com.example.order_mgnt.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final WishlistRepository wishlistRepository;
    private final CartRepository cartRepository;

    @Override
    public OrderResponseDTO create(OrderRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderRequest cannot be null");
        }
        User user=null;
        if(dto.getUserId()!= null) {
            user=userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        }else{
            throw new ResourceNotFoundException("User not found with id: " + dto.getUserId());
        }

        try {
            Order order=new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.PENDING);
            order.setTotalAmount(dto.getTotalAmount());
            Set<OrderItem> orderItems = dto.getItems().stream()
                    .map(itemDTO -> {
                        OrderItem item = new OrderItem();
                        item.setBook(bookRepository.findById(itemDTO.getBookId()).get());
                        item.setOrder(order);
                        item.setQuantity(itemDTO.getQuantity());
                        item.setPrice(itemDTO.getPrice());
                        return item;
                    })
                    .collect(Collectors.toSet());
            order.setItems(orderItems);
            orderRepository.save(order);
            return modelMapper.map(order, OrderResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    @Override
    public OrderResponseDTO getById(UUID id) {
        return orderRepository.findById(id)
                .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<OrderResponseDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO update(UUID id, OrderRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderRequest cannot be null");
        }
        User user=null;
        if(dto.getUserId()!= null) {
            user=userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        }else{
            throw new ResourceNotFoundException("User not found with id: " + dto.getUserId());
        }
        try {
            Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order.setTotalAmount(dto.getTotalAmount());
            Set<OrderItem> orderItems = dto.getItems().stream()
                    .map(itemDTO -> {
                        OrderItem item = new OrderItem();
                        item.setBook(bookRepository.findById(itemDTO.getBookId()).get());
                        item.setOrder(order);
                        item.setQuantity(itemDTO.getQuantity());
                        item.setPrice(itemDTO.getPrice());
                        return item;
                    })
                    .collect(Collectors.toSet());
            order.setItems(orderItems);
            orderRepository.save(order);
            return modelMapper.map(order, OrderResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error checking order existence: " + e.getMessage());
        }

    }

    @Override
    public void delete(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponseDTO convertWishlistToOrder(List<UUID> wishlistIds) {
        if (wishlistIds == null || wishlistIds.isEmpty()) {
            throw new IllegalArgumentException("Wishlist is empty");
        }
        User user=wishlistRepository.findById(wishlistIds.get(0))
                .map(Wishlist::getUser)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + wishlistIds.get(0)));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        Set<OrderItem> orderItems = wishlistIds.stream().map(item -> {
            Book book=bookRepository.findById(item).get();
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setQuantity(1);
            orderItem.setPrice(book.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toSet());
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO convertCartToOrder(UUID cartIds) {
        if (cartIds ==null || cartIds.toString().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        Cart cart=cartRepository.findById(cartIds)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartIds));
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        Set<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toSet());

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // Optional: clear or delete cart after placing order
        cartRepository.delete(cart);

        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }
}
