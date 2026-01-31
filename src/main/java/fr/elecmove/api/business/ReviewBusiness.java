package fr.elecmove.api.business;

import fr.elecmove.api.model.Review;
import fr.elecmove.api.model.User;

import java.util.List;

public interface ReviewBusiness {

    /**
     *
     * @param review
     * @param user
     * @return
     */
    Review createReview(Review review, User user);


    /**
     *
     * @param id
     * @return
     */
    Review getReview(String id);


    /**
     *
     * @param email
     * @return
     */
    List<Review> getAllReviewByEmail(String email);


    /**
     *
     * @param id
     * @param review
     * @param user
     * @return
     */
    Review updateReview(String id, Review review, User user);


    /**
     *
     * @param id
     * @return
     */
    void deleteReview(String id, User user);


}
