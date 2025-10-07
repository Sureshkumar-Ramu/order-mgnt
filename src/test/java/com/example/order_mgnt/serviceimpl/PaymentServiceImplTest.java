package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.PaymentRequestDTO;
import com.example.order_mgnt.dto.response.PaymentResponseDTO;
import com.example.order_mgnt.entity.Payment;
import com.example.order_mgnt.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ModelMapper modelMapper;

    private UUID paymentId;
    private Payment payment;
    private PaymentRequestDTO requestDTO;
    private PaymentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentId = UUID.randomUUID();

        payment = new Payment();
        payment.setId(paymentId);

        requestDTO = new PaymentRequestDTO();
        responseDTO = new PaymentResponseDTO();
        responseDTO.setId(paymentId);
    }

    @Test
    void testCreatePayment() {
        when(modelMapper.map(requestDTO, Payment.class)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.create(requestDTO);
        assertNotNull(result);
        assertEquals(paymentId, result.getId());
    }

    @Test
    void testGetPaymentById() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.getById(paymentId);
        assertNotNull(result);
        assertEquals(paymentId, result.getId());
    }

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(responseDTO);

        List<PaymentResponseDTO> result = paymentService.getAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void testUpdatePayment() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.update(paymentId, requestDTO);
        assertNotNull(result);
        assertEquals(paymentId, result.getId());
    }

    @Test
    void testDeletePayment() {
        when(paymentRepository.existsById(paymentId)).thenReturn(true);
        doNothing().when(paymentRepository).deleteById(paymentId);

        assertDoesNotThrow(() -> paymentService.delete(paymentId));
        verify(paymentRepository, times(1)).deleteById(paymentId);
    }
}

