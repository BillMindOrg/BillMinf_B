package pe.edu.upc.billmind_backend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.billmind_backend.auth.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
