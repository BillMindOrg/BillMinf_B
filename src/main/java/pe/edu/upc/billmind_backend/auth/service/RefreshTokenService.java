package pe.edu.upc.billmind_backend.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.billmind_backend.auth.entities.RefreshToken;
import pe.edu.upc.billmind_backend.auth.repository.RefreshTokenRepository;
import pe.edu.upc.billmind_backend.domain.models.Client;
import pe.edu.upc.billmind_backend.domain.repositories.ClientRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final ClientRepository clientRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(ClientRepository clientRepository, RefreshTokenRepository refreshTokenRepository) {
        this.clientRepository = clientRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String mail) {
        Client client = clientRepository.findByMail(mail).orElseThrow(() -> new UsernameNotFoundException("Client not found with mail: " + mail));
        RefreshToken refreshToken = client.getRefreshToken();
        if (refreshToken == null) {
            long refreshTokenDuration = 5*60*60*10000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenDuration))
                    .client(client)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException("Refresh token not found"));
        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token has expired");
        }
        return refToken;
    }
}
