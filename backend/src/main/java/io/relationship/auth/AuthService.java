package io.relationship.auth;

import io.relationship.common.exception.DuplicateResourceException;
import io.relationship.security.JwtTokenProvider;
import io.relationship.user.UserEntity;
import io.relationship.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken: " + request.getUsername());
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName() != null
                        ? request.getDisplayName() : request.getUsername())
                .accountType(UserEntity.AccountType.PRIVATE)
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        String accessToken  = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshTokenFromUsername(user.getEmail());

        return buildResponse(user, accessToken, refreshToken);
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String accessToken  = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return buildResponse(user, accessToken, refreshToken);
    }

    public AuthDto.AuthResponse refreshToken(AuthDto.RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        String email       = jwtTokenProvider.getUsernameFromRefreshToken(refreshToken);
        UserEntity user    = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken  = jwtTokenProvider.generateTokenFromUsername(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshTokenFromUsername(email);

        return buildResponse(user, newAccessToken, newRefreshToken);
    }

    private AuthDto.AuthResponse buildResponse(UserEntity user, String accessToken, String refreshToken) {
        return AuthDto.AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
