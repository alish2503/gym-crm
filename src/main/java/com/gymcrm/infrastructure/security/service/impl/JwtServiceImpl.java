package com.gymcrm.infrastructure.security.service.impl;

import com.gymcrm.infrastructure.security.service.port.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * @author Alish
 */
@Service
public class JwtServiceImpl implements JwtService {
    private final Key key;
    private final long expirationMs;
    private final JwtParser parser;

    public JwtServiceImpl(@Value("${security.jwt.secret}") String secret,
                          @Value("${security.jwt.expiration-ms}") long expirationMs) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder().setSubject(username).setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public boolean isValidToken(String token) {
        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Date getExpiration(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}
