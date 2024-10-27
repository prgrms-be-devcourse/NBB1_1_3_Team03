package com.sscanner.team.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}")String secret){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String getCategory(String token){
        return parseToken(token).get("category", String.class);
    }

    public String getEmail(String token) {
        return parseToken(token).get("email", String.class);    }

    public String getAuthority(String token) {
        return parseToken(token).get("authority", String.class);    }


    public void isExpired(String token) {
        Claims claims = parseToken(token);

        if (claims.getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, claims, "토큰 만료됨");
    }
}

    public String createJwt(String category ,String email, String authority, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("email", email)
                .claim("authority", authority)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }






}
