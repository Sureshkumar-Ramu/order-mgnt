package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.common.BookQuantityUpdateMessage;
import com.example.order_mgnt.dto.request.PaymentRequestDTO;
import com.example.order_mgnt.dto.response.PaymentResponseDTO;
import com.example.order_mgnt.entity.Order;
import com.example.order_mgnt.entity.Payment;
import com.example.order_mgnt.exception.ResourceNotFoundException;
import com.example.order_mgnt.rabbitmq.publisher.BookUpdatePublisher;
import com.example.order_mgnt.repository.OrderRepository;
import com.example.order_mgnt.repository.PaymentRepository;
import com.example.order_mgnt.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final BookUpdatePublisher publisher;

    @Override
    public PaymentResponseDTO create(PaymentRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("PaymentRequest cannot be null");
        }
        if (dto.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        try {

            Payment payment=new Payment();
            payment.setOrder(orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId())));
            payment.setAmount(dto.getAmount());
            payment.setPayment_method(Payment.PaymentMethod.UPI);
            payment.setPayment_date(LocalDateTime.now());
            payment.setStatus(Payment.PaymentStatus.SUCCESS);

            // Fetch the associated order
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId()));
            payment.setOrder(order);

            payment=paymentRepository.save(payment);

            // If payment is successful, update order status
            if ("SUCCESS".equalsIgnoreCase(payment.getStatus().name())) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                orderRepository.save(order);
                order.getItems().stream()
                        .forEach(item -> {
                            publisher.sendUpdate(new BookQuantityUpdateMessage(item.getBook().getId(),item.getQuantity()));
                        });

            }

            return modelMapper.map(paymentRepository.save(payment), PaymentResponseDTO.class);
        }catch (Exception e) {
            throw new RuntimeException("Error creating payment: " + e.getMessage());
        }

    }

    @Override
    public PaymentResponseDTO getById(UUID id) {
        return paymentRepository.findById(id)
                .map(payment -> modelMapper.map(payment, PaymentResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public List<PaymentResponseDTO> getAll() {
        return paymentRepository.findAll().stream()
                .map(payment -> modelMapper.map(payment, PaymentResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO update(UUID id, PaymentRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("PaymentRequest cannot be null");
        }
        if (dto.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        try {
            Payment payment = paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
            payment.setOrder(orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId())));
            payment.setAmount(dto.getAmount());
            payment.setPayment_method(Payment.PaymentMethod.UPI);
            payment.setPayment_date(LocalDateTime.now());
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            // Fetch the associated order
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId()));
            payment.setOrder(order);

            payment=paymentRepository.save(payment);

            // If payment is successful, update order status
            if ("SUCCESS".equalsIgnoreCase(payment.getStatus().name())) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }
            return modelMapper.map(paymentRepository.save(payment), PaymentResponseDTO.class);
        }catch (Exception e) {
            throw new RuntimeException("Error updating payment: " + e.getMessage());
        }

    }

    @Override
    public void delete(UUID id) {
        paymentRepository.deleteById(id);
    }
}
