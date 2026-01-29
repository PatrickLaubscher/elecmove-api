package fr.elecmove.api;


import fr.elecmove.api.messaging.MailService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiUserTest {

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

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getUserShouldReturnUserConnected() throws Exception {
        mvc.perform(get("/api/users/me")
                        .with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("firstname1"))
                .andExpect(jsonPath("$.lastname").value("lastname1"))
                .andExpect(jsonPath("$.email").value("firstname1@test.com"));
    }

    @Test
    void putUserShouldUpdateUser() throws Exception {
        mvc.perform(put("/api/users/me")
                .with(user(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
			{
				"firstname": "updated firstname"
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("updated firstname"));
    }

}
