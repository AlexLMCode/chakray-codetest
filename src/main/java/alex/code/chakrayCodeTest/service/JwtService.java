package alex.code.chakrayCodeTest.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import alex.code.chakrayCodeTest.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long expirationTimeInMillis;

    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getTaxId())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
    }

    public String extractTaxId(String token) {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractTaxId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
