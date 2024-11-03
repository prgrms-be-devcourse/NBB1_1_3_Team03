package com.sscanner.team.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtil(@Value("\${jwt.secret}") secret: String) {
    private val secretKey: SecretKey = SecretKeySpec(
        secret.toByteArray(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().algorithm
    )

    private fun parseToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getCategory(token: String): String {
        return parseToken(token).get("category", String::class.java)
    }

    fun getEmail(token: String): String {
        return parseToken(token).get("email", String::class.java)
    }

    fun getAuthority(token: String): String {
        return parseToken(token).get("authority", String::class.java)
    }


    fun isExpired(token: String) {
        val claims = parseToken(token)

        if (claims.expiration.before(Date())) {
            throw ExpiredJwtException(null, claims, "토큰 만료됨")
        }
    }

    fun createJwt(category: String, email: String, authority: String, expiredMs: Long): String {
        return Jwts.builder()
            .claim("category", category)
            .claim("email", email)
            .claim("authority", authority)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact()
    }
}
