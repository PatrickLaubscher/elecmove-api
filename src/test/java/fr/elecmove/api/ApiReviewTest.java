package fr.elecmove.api;

import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.Review;
import fr.elecmove.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiReviewTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    EntityManager em;

    @MockitoBean
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    User user1 = new User();
    String reviewId;

    @BeforeEach
    void setUp() throws Exception {

        doNothing().when(mailService).sendEmailValidation(any(User.class), anyString());

        user1.setFirstname("firstname1");
        user1.setLastname("lastname1");
        user1.setEmail("firstname1@test.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRole("ROLE_USER");
        user1.setValidated(true);
        em.persist(user1);

        Review review = new Review();
        review.setUser(user1);
        review.setComment("Super service !");
        review.setRate(5);
        em.persist(review);
        reviewId = review.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getWithIdShouldReturnReview() throws Exception {
        mvc.perform(get("/api/reviews/"+reviewId).with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Super service !"))
                .andExpect(jsonPath("$.rate").value(5));
    }

    @Test
    void getWithIdShouldThrow404IfNoReview() throws Exception {
        mvc.perform(get("/api/reviews/dontexist").with(user(user1)))
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllShouldReturnReviewList() throws Exception {
        mvc.perform(get("/api/reviews").with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void postShouldPersistReviewWithRightUser() throws Exception {

        mvc.perform(post("/api/reviews")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"comment":"Excellent !",
				"rate": 4
			}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value("Excellent !"))
                .andExpect(jsonPath("$.rate").value(4))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void putShouldUpdateReview() throws Exception {
        mvc.perform(put("/api/reviews/"+reviewId)
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"comment":"Commentaire modifié"
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Commentaire modifié"));
    }

    @Test
    void deleteShouldDeleteReviewById() throws Exception {
        mvc.perform(delete("/api/reviews/"+reviewId).with(user(user1)))
                .andExpect(status().isNoContent());
    }


}
