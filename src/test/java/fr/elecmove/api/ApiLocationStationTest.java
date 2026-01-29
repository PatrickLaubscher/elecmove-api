package fr.elecmove.api;

import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.LocationStation;


import fr.elecmove.api.model.User;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiLocationStationTest {

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

    String locationId;

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


        LocationStation location = new LocationStation();
        location.setAddress("address1");
        location.setCity("city1");
        location.setZipcode("zipcode1");
        location.setLatitude(BigDecimal.valueOf(10.0));
        location.setLongitude(BigDecimal.valueOf(10.0));
        em.persist(location);
        locationId = location.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getWithIdShouldReturnLocationStation() throws Exception {
        mvc.perform(get("/api/locations/"+locationId).with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("address1"))
                .andExpect(jsonPath("$.city").value("city1"))
                .andExpect(jsonPath("$.zipcode").value("zipcode1"))
                .andExpect(jsonPath("$.latitude").value(10.0))
                .andExpect(jsonPath("$.longitude").value(10.0));
    }

    @Test
    void getWithIdShouldThrow404IfNoLocationStation() throws Exception {
        mvc.perform(get("/api/locations/dontexist").with(user(user1)))
                .andExpect(status().isNotFound());
    }


    @Test
    void postShouldPersistLocationStation() throws Exception {

        mvc.perform(post("/api/locations")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"address":"4 rue de la paix",
			    "city": "ville",
			    "zipcode": "12345",
			    "latitude": 11.00,
				"longitude": 11.00
			}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value("4 rue de la paix"))
                .andExpect(jsonPath("$.city").value("ville"))
                .andExpect(jsonPath("$.zipcode").value("12345"))
                .andExpect(jsonPath("$.latitude").value(11.00))
                .andExpect(jsonPath("$.longitude").value(11.00));
    }

    @Test
    void deleteShouldDeleteLocationStationById() throws Exception {
        mvc.perform(delete("/api/locations/"+locationId).with(user(user1)))
                .andExpect(status().isNoContent());
    }

}
