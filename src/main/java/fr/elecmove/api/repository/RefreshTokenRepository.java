package fr.elecmove.api.repository;

import fr.elecmove.api.model.RefreshToken;
import fr.elecmove.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiresAt < NOW()")
    void deleteExpired();
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.user = :user")
    void deleteByUser(User user);

}
