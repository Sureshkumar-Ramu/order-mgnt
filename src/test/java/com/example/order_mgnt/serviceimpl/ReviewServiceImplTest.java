package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.ReviewRequestDTO;
import com.example.order_mgnt.dto.response.ReviewResponseDTO;
import com.example.order_mgnt.entity.Review;
import com.example.order_mgnt.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ModelMapper modelMapper;

    private UUID reviewId;
    private Review review;
    private ReviewRequestDTO requestDTO;
    private ReviewResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reviewId = UUID.randomUUID();

        review = new Review();
        review.setId(reviewId);

        requestDTO = new ReviewRequestDTO();
        responseDTO = new ReviewResponseDTO();
        responseDTO.setId(reviewId);
    }

    @Test
    void testCreateReview() {
        when(modelMapper.map(requestDTO, Review.class)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(modelMapper.map(review, ReviewResponseDTO.class)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.create(requestDTO);
        assertNotNull(result);
        assertEquals(reviewId, result.getId());
    }

    @Test
    void testGetReviewById() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(modelMapper.map(review, ReviewResponseDTO.class)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.getById(reviewId);
        assertNotNull(result);
        assertEquals(reviewId, result.getId());
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));
        when(modelMapper.map(review, ReviewResponseDTO.class)).thenReturn(responseDTO);

        List<ReviewResponseDTO> result = reviewService.getAll();
        assertFalse(result.isEmpty());
    }

    @Test
    void testUpdateReview() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);
        when(modelMapper.map(review, ReviewResponseDTO.class)).thenReturn(responseDTO);

        ReviewResponseDTO result = reviewService.update(reviewId, requestDTO);
        assertNotNull(result);
        assertEquals(reviewId, result.getId());
    }

    @Test
    void testDeleteReview() {
        when(reviewRepository.existsById(reviewId)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(reviewId);

        assertDoesNotThrow(() -> reviewService.delete(reviewId));
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }
}

