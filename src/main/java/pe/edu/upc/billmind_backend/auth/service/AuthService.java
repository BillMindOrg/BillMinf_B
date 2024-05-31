package pe.edu.upc.billmind_backend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.billmind_backend.auth.entities.ClientRole;
import pe.edu.upc.billmind_backend.auth.utils.AuthResponse;
import pe.edu.upc.billmind_backend.auth.utils.LogInRequest;
import pe.edu.upc.billmind_backend.auth.utils.RegisterRequest;
import pe.edu.upc.billmind_backend.domain.models.Client;
import pe.edu.upc.billmind_backend.domain.repositories.ClientRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        var client = Client.builder()
                .name(registerRequest.getName())
                .last_name(registerRequest.getLast_name())
                .phone(registerRequest.getPhone())
                .mail(registerRequest.getMail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(ClientRole.USER)
                .build();

        Client savedClient = clientRepository.save(client);
        var accesToken = jwtService.generateToken(savedClient);
        var refreshToken = refreshTokenService.createRefreshToken(savedClient.getMail());

        return AuthResponse.builder()
                .accesToken(accesToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse login(LogInRequest logInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInRequest.getMail(), logInRequest.getPassword())
        );

        var client = clientRepository.findByMail(logInRequest.getMail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var accessToken = jwtService.generateToken(client);
        var refreshToken = refreshTokenService.createRefreshToken(client.getMail());

        return AuthResponse.builder()
                .accesToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}
