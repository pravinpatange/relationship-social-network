package io.relationship.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    // ── Signing key ──────────────────────────────────────────────────────────

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ── Access token ─────────────────────────────────────────────────────────

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        return buildToken(username, jwtExpirationMs, "ACCESS");
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return "ACCESS".equals(claims.get("type", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid access token: {}", e.getMessage());
            return false;
        }
    }

    // ── Refresh token ────────────────────────────────────────────────────────

    public String generateRefreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateRefreshTokenFromUsername(userDetails.getUsername());
    }

    public String generateRefreshTokenFromUsername(String username) {
        return buildToken(username, refreshExpirationMs, "REFRESH");
    }

    public String getUsernameFromRefreshToken(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            return "REFRESH".equals(claims.get("type", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    private String buildToken(String username, long expiryMs, String type) {
        return Jwts.builder()
                .subject(username)
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiryMs))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
