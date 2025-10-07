package com.example.order_mgnt.serviceimpl;

import com.example.order_mgnt.dto.request.ReviewRequestDTO;
import com.example.order_mgnt.dto.response.ReviewResponseDTO;
import com.example.order_mgnt.entity.Review;
import com.example.order_mgnt.exception.ResourceNotFoundException;
import com.example.order_mgnt.repository.BookRepository;
import com.example.order_mgnt.repository.ReviewRepository;
import com.example.order_mgnt.repository.UserRepository;
import com.example.order_mgnt.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ReviewRequest cannot be null");
        }
        try {
            Review review=new Review();
            if (dto.getBookId() != null) {
                review.setBook(bookRepository.findById(dto.getBookId())
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBookId())));
            }
            if(dto.getUserId()!= null) {
                review.setUser(userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId())));
            }
            review.setReview_date(LocalDateTime.now());
            review.setComment(dto.getComment());
            review.setRating(dto.getRating());
            review=reviewRepository.save(review);
            return modelMapper.map(reviewRepository.save(review), ReviewResponseDTO.class);
        }catch (Exception e) {
            throw new RuntimeException("Error creating review: " + e.getMessage());
        }

    }

    @Override
    public ReviewResponseDTO getById(UUID id) {
        return reviewRepository.findById(id)
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    @Override
    public List<ReviewResponseDTO> getAll() {
        return reviewRepository.findAll().stream()
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDTO update(UUID id, ReviewRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ReviewRequest cannot be null");
        }
        try {
            Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
            if (dto.getBookId() != null) {
                review.setBook(bookRepository.findById(dto.getBookId())
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBookId())));
            }
            if(dto.getUserId()!= null) {
                review.setUser(userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId())));
            }
            review.setReview_date(LocalDateTime.now());
            review.setComment(dto.getComment());
            review.setRating(dto.getRating());
            review=reviewRepository.save(review);
            return modelMapper.map(reviewRepository.save(review), ReviewResponseDTO.class);
        }catch (Exception e) {
            throw new RuntimeException("Error updating review: " + e.getMessage());
        }

    }

    @Override
    public void delete(UUID id) {
        reviewRepository.deleteById(id);
    }
}
