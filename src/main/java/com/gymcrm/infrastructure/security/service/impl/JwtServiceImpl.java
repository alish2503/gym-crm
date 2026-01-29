package com.gymcrm.infrastructure.security.service.impl;

import com.gymcrm.infrastructure.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * @author Alish
 */

@Service
public class JwtServiceImpl implements JwtService {
    private final Key key;
    private final JwtParser parser;

    @Setter
    @Value("${security.jwt.user.expiration-ms}")
    private long expirationMs;

    @Setter
    @Value("${security.jwt.user.expiration-ms}")
    private long serviceExpirationMs;

    public JwtServiceImpl(@Value("${security.jwt.secret}") String secret) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    @Override
    public String generateTokenForUser(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder().setSubject(username).setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    @Override
    public String generateTokenForService()
    {
        Date now = new Date();
        Date exp = new Date(now.getTime() + serviceExpirationMs);
        return Jwts.builder().claim("roles", List.of("SERVICE")).setExpiration(exp).
                signWith(key, SignatureAlgorithm.HS256).compact();
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getUsername(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public Date getExpiration(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}
