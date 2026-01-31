package fr.elecmove.api.repository;


import fr.elecmove.api.model.Car;
import fr.elecmove.api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    List<Review> findReviewByUserEmail(String email);

    List<Review> findTop3ByRateGreaterThanEqualOrderByCreatedAtDesc(int rate);

}