package fr.elecmove.api.controller;

import fr.elecmove.api.business.ReviewBusiness;
import fr.elecmove.api.controller.dto.review.ReviewCreationDTO;
import fr.elecmove.api.controller.dto.review.ReviewDTO;
import fr.elecmove.api.controller.dto.mapper.ReviewMapper;
import fr.elecmove.api.model.Review;
import fr.elecmove.api.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    ReviewBusiness ReviewBusiness;
    ReviewMapper ReviewMapper;

    public ReviewController(ReviewBusiness ReviewBusiness, ReviewMapper ReviewMapper) {
        this.ReviewBusiness = ReviewBusiness;
        this.ReviewMapper = ReviewMapper;
    }

    @GetMapping("/{id}")
    public ReviewDTO getReview(@PathVariable String id) {
        return ReviewMapper.toDto(ReviewBusiness.getReview(id));
    }


    @GetMapping
    public List<ReviewDTO> getAllReviews(@AuthenticationPrincipal UserDetails userDetails) {
        List<ReviewDTO> ReviewDTOS = new ArrayList<>();
        for(Review Review : ReviewBusiness.getAllReviewByEmail(userDetails.getUsername())){
            ReviewDTOS.add(ReviewMapper.toDto(Review));
        }
        return ReviewDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO createReview(@RequestBody @Valid ReviewCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ReviewMapper.toDto(
                ReviewBusiness.createReview(ReviewMapper.toEntity(dto), user)
        );
    }


    @PutMapping("/{id}")
    public ReviewDTO updateReview(@PathVariable String id, @RequestBody ReviewCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ReviewMapper.toDto(
                ReviewBusiness.updateReview(id, ReviewMapper.toEntity(dto), user)
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        ReviewBusiness.deleteReview(id, user);
    }
}
