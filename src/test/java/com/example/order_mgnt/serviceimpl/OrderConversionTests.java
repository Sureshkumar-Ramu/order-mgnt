package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.PaymentRequestDTO;
import com.example.order_mgnt.dto.response.OrderResponseDTO;
import com.example.order_mgnt.entity.*;
import com.example.order_mgnt.entity.book_mgnt.Book;
import com.example.order_mgnt.entity.book_mgnt.User;
import com.example.order_mgnt.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderConversionTests {

    @Mock private WishlistRepository wishlistRepository;
    @Mock private CartRepository cartRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private PaymentRepository paymentRepository;

    @InjectMocks private OrderServiceImpl orderService;
    @InjectMocks private PaymentServiceImpl paymentService;

    @Mock private ModelMapper modelMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(
                orderRepository, modelMapper,
                mock(UserRepository.class),
                mock(BookRepository.class),
                wishlistRepository,
                cartRepository
        );
        paymentService = new PaymentServiceImpl(
                paymentRepository, modelMapper,
                orderRepository
        );
    }

    @Test
    void testConvertWishlistToOrder() {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(UUID.randomUUID());
        wishlist.setUser(new User());
        wishlist.setBook(new Book());
        wishlist.setAddedAt(LocalDateTime.now());

        when(wishlistRepository.findById(wishlist.getId())).thenReturn(Optional.of(wishlist));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        OrderResponseDTO order = orderService.convertWishlistToOrder(List.of(wishlist.getId()));
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testConvertCartToOrder() {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setBook(new Book());
        item.setQuantity(1);
        cart.setItems(Set.of(item));

        when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        OrderResponseDTO order = orderService.convertCartToOrder(cart.getId());
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testConfirmOrderOnSuccessfulPayment() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.OrderStatus.PENDING);

        PaymentRequestDTO dto = new PaymentRequestDTO();
        dto.setOrderId(orderId);
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD.name());
        dto.setStatus("SUCCESS");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        paymentService.create(dto);

        assertEquals("CONFIRMED", order.getStatus());
    }
}
