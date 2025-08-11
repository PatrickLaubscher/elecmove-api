package fr.elecmove.api;

import fr.elecmove.api.model.RefreshToken;
import fr.elecmove.api.model.Role;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.RefreshTokenRepository;
import fr.elecmove.api.repository.RoleRepository;
import fr.elecmove.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;


@SpringBootTest
@Transactional
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testDeleteByUser() {

        Role roleUser = roleRepository.findByName("ROLE_USER").orElse(null);

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("pass");
        user.setRole(roleUser);

        user = userRepository.saveAndFlush(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        refreshTokenRepository.saveAndFlush(token);

        List<RefreshToken> tokensBefore = refreshTokenRepository.findByUser(user);
        assertFalse(tokensBefore.isEmpty());

        refreshTokenRepository.deleteByUser(user);

        refreshTokenRepository.flush();

        List<RefreshToken> tokensAfter = refreshTokenRepository.findByUser(user);
        assertTrue(tokensAfter.isEmpty());
    }

}
