package fr.elecmove.api;


import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.*;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiFavoriteStationTest {


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
    Station station1 = new Station();
    Station station2 = new Station();
    FavoriteStation favoriteStation1 = new FavoriteStation();
    String station1Id;
    String station2Id;
    String favoriteId;

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

        station1.setName("station1");
        station1.setTarification(1.0);
        station1.setType("type1");
        station1.setPower("power1");
        station1.setInstruction("instruction1");
        station1.setFreeStanding(true);
        em.persist(station1);
        station1Id = station1.getId();

        station2.setName("station2");
        station2.setTarification(1.0);
        station2.setType("type1");
        station2.setPower("power1");
        station2.setInstruction("instruction1");
        station2.setFreeStanding(true);
        em.persist(station2);
        station2Id = station2.getId();

        favoriteStation1.setStation(station1);
        favoriteStation1.setUser(user1);
        em.persist(favoriteStation1);
        favoriteId = favoriteStation1.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getWithIdShouldReturnFavoriteStationWithRightUserAndRightStation() throws Exception {
        mvc.perform(get("/api/favorite-stations/"+favoriteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station.name").value(station1.getName()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void getWithIdShouldThrow404IfNoFavoriteStation() throws Exception {
        mvc.perform(get("/api/favorite-stations/dontexist"))
                .andExpect(status().isNotFound());
    }


    @Test
    void postShouldPersistFavoriteStationWithRightStationAndRightUser() throws Exception {

        mvc.perform(post("/api/favorite-stations")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"stationId": "%s"
			}""".formatted(station2Id)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.station.name").value(station2.getName()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }


    @Test
    void postShouldNotPersistFavoriteStationWithStationAlreadyFavourite() throws Exception {

        mvc.perform(post("/api/favorite-stations")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"stationId": "%s"
			}""".formatted(station1Id)))
                .andExpect(status().isConflict());
    }


    @Test
    void deleteShouldDeleteFavoriteStationById() throws Exception {
        mvc.perform(delete("/api/favorite-stations/"+favoriteId).with(user(user1)))
                .andExpect(status().isNoContent());
    }


}
