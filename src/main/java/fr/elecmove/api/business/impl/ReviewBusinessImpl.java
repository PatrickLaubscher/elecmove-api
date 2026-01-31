package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.ReviewBusiness;
import fr.elecmove.api.business.mapper.ReviewEntityMapper;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.Review;
import fr.elecmove.api.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ReviewBusinessImpl implements ReviewBusiness {

    private final ReviewRepository reviewRepository;
    private final ReviewEntityMapper reviewEntityMapper;


    public ReviewBusinessImpl(ReviewRepository reviewRepository, ReviewEntityMapper reviewEntityMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewEntityMapper = reviewEntityMapper;
    }

    @Override
    public Review createReview(Review review, User user) {
        review.setUser(user);
        return reviewRepository.save(review);
    }


    @Override
    public Review getReview(String id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The review does not exist")
        );
    }


    @Override
    public List<Review> getAllReviewByEmail(String email) {
        return reviewRepository.findReviewByUserEmail(email);
    }


    @Override
    public Review updateReview(String id, Review review, User user) {

        Review existingReview = reviewRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Review does not exist")
        );
        if(!existingReview.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't modify this Review.");
        }

        reviewEntityMapper.merge(existingReview, review);

        return reviewRepository.save(existingReview);
    }


    @Override
    public void deleteReview(String id, User user) {

        Review existingReview = getReview(id);

        if(!existingReview.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this Review.");
        }

        reviewRepository.delete(existingReview);
    }

    @Override
    public List<Review> getTopReviews() {
        return reviewRepository.findTop3ByRateGreaterThanEqualOrderByCreatedAtDesc(4);
    }

}
