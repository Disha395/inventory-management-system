package com.example.ims_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    private static final long EXPIRATION_TIME_IN_MILLISEC =
            1000L * 60L * 60L * 24L * 30L * 6L; // 6 months

    private SecretKey key;

    @Value("${secretJwtString}")
    private String secretJwtString;

    @PostConstruct
    private void init() {
        // Recommended way in 0.11.5
        this.key = Keys.hmacShaKeyFor(secretJwtString.getBytes(StandardCharsets.UTF_8));
    }

    // ================= GENERATE TOKEN =================
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISEC))
                .signWith(key) // HS256 automatically chosen
                .compact();
    }

    // ================= EXTRACT USERNAME =================
    public String getUsernameFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // ================= EXTRACT CLAIMS =================
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(claims);
    }

    // ================= VALIDATE TOKEN =================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ================= CHECK EXPIRY =================
    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}