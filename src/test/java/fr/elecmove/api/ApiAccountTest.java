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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.http.MediaType;



@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiAccountTest {

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
    void postShouldPersistUserAndSendEmail() throws Exception {
        mvc.perform(post("/api/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"firstname":"firstname2",
				"lastname":"lastname2",
				"email":"firstname2@test.com",
				"password": "test",
			    "role": "ROlE_USER"
			}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("firstname2"))
                .andExpect(jsonPath("$.lastname").value("lastname2"))
                .andExpect(jsonPath("$.email").value("firstname2@test.com"));
        verify(mailService, times(1)).sendEmailValidation(any(User.class), anyString());
    }



    @Test
    void DeleteAccountShouldAnonymizePrivateInformationAndDisableAccount() throws Exception {

        User userBefore = userRepository.findByEmail("firstname1@test.com")
                .orElseThrow();

        mvc.perform(patch("/api/account/delete-account")
                .with(user(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Your account has been deleted"));

        User updatedUser = userRepository.findById(userBefore.getId()).orElseThrow();

        assertEquals("anonymous", updatedUser.getFirstname());
        assertEquals("anonymous", updatedUser.getLastname());
        assertEquals("anonymous", updatedUser.getEmail());
        assertFalse(updatedUser.getValidated());
    }




}
