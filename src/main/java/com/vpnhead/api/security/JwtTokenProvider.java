package com.vpnhead.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys; // Import this
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import jakarta.annotation.PostConstruct; // Import this

@Component
public class JwtTokenProvider {

    // We will no longer directly inject 'jwt.secret' if we're auto-generating
    // @Value("${jwt.secret}")
    // private String jwtSecretString;

    @Value("${jwt.expiration}")
    private long expirationTime;

    // The secure key
    private Key signingKey; // No longer final, as it's initialized in PostConstruct

    // You can keep a placeholder secret in application.properties if you want,
    // but it won't be used for signing if we auto-generate.
    // It could be used as a "seed" for generation, but Keys.secretKeyFor is better.

    @PostConstruct
    public void init() {
        // This method runs after dependency injection is complete.
        // Generates a new cryptographically strong key for HS256 every time the application starts.
        this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // FOR DEBUGGING ONLY: Print the generated key (remove in production!)
        // System.out.println("Generated JWT Secret (Base64 Encoded): " + java.util.Base64.getEncoder().encodeToString(signingKey.getEncoded()));
    }

    public String generateToken(String username) {
        // Ensure signingKey is initialized (it will be by @PostConstruct)
        if (signingKey == null) {
            throw new IllegalStateException("JWT signing key not initialized!");
        }
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Ensure signingKey is initialized
        if (signingKey == null) {
            throw new IllegalStateException("JWT signing key not initialized!");
        }
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String username) {
        // Ensure signingKey is initialized
        if (signingKey == null) {
            throw new IllegalStateException("JWT signing key not initialized!");
        }
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (SignatureException ex) {
            System.err.println("Invalid JWT signature: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty: " + ex.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}