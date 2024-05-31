package pe.edu.upc.billmind_backend.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.billmind_backend.auth.entities.RefreshToken;
import pe.edu.upc.billmind_backend.auth.service.AuthService;
import pe.edu.upc.billmind_backend.auth.service.JwtService;
import pe.edu.upc.billmind_backend.auth.service.RefreshTokenService;
import pe.edu.upc.billmind_backend.auth.utils.AuthResponse;
import pe.edu.upc.billmind_backend.auth.utils.LogInRequest;
import pe.edu.upc.billmind_backend.auth.utils.RefreshTokenRequest;
import pe.edu.upc.billmind_backend.auth.utils.RegisterRequest;
import pe.edu.upc.billmind_backend.domain.models.Client;

@RestController
@RequestMapping("/api/v1/auth")
@EnableWebSecurity
@EnableMethodSecurity
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(@Lazy AuthService authService, @Lazy RefreshTokenService refreshTokenService, @Lazy JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LogInRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        Client client = refreshToken.getClient();
        String accessToken = jwtService.generateToken(client);

        return ResponseEntity.ok(AuthResponse.builder()
                        .accesToken(accessToken)
                        .refreshToken(refreshToken.getRefreshToken())
                .build());
    }
}
