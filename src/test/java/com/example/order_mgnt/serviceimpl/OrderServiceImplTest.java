package com.example.order_mgnt.serviceimpl;


import com.example.order_mgnt.dto.request.OrderRequestDTO;
import com.example.order_mgnt.dto.response.OrderResponseDTO;
import com.example.order_mgnt.entity.Order;
import com.example.order_mgnt.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ModelMapper modelMapper;

    private UUID orderId;
    private Order order;
    private OrderRequestDTO orderRequestDTO;
    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderId = UUID.randomUUID();

        order = new Order();
        order.setId(orderId);

        orderRequestDTO = new OrderRequestDTO();
        orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(orderId);
    }

    @Test
    void testCreateOrder() {
        when(modelMapper.map(orderRequestDTO, Order.class)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

        OrderResponseDTO response = orderService.create(orderRequestDTO);
        assertNotNull(response);
        assertEquals(orderId, response.getId());
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

        OrderResponseDTO response = orderService.getById(orderId);
        assertNotNull(response);
        assertEquals(orderId, response.getId());
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = Collections.singletonList(order);
        when(orderRepository.findAll()).thenReturn(orders);
        when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

        List<OrderResponseDTO> responses = orderService.getAll();
        assertFalse(responses.isEmpty());
    }

    @Test
    void testUpdateOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(modelMapper.map(order, OrderResponseDTO.class)).thenReturn(orderResponseDTO);

        OrderResponseDTO response = orderService.update(orderId, orderRequestDTO);
        assertNotNull(response);
        assertEquals(orderId, response.getId());
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.existsById(orderId)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(orderId);

        assertDoesNotThrow(() -> orderService.delete(orderId));
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}

