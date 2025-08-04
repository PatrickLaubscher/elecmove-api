package fr.elecmove.api;


import fr.elecmove.api.messaging.MailService;

import fr.elecmove.api.model.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
public class ApiUserTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    EntityManager em;

    @MockitoBean
    private MailService mailService;


    @BeforeEach
    void setUp() throws Exception {
        doNothing().when(mailService).sendEmailValidation(any(User.class), anyString());
    }


    @Test
    void postShouldPersistUserAndSendEmail() throws Exception {
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"firstname":"firstname1",
				"lastname":"lastname1",
				"email":"firstname1@test.com",
				"password": "test"
			}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("firstname1"))
                .andExpect(jsonPath("$.lastname").value("lastname1"))
                .andExpect(jsonPath("$.email").value("firstname1@test.com"))
                .andExpect(jsonPath("$.id").isNotEmpty());
        verify(mailService, times(1)).sendEmailValidation(any(User.class), anyString());
    }




}
