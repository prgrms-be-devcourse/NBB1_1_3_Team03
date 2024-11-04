package com.sscanner.team.auth.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisRefreshTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {

    // 리프레시 토큰 저장
    fun save(email: String, refreshToken: String, expirationTime: Long) {
        redisTemplate.opsForValue().set(email, refreshToken, expirationTime, TimeUnit.MILLISECONDS)
    }

    // 저장된 리프레시 토큰 조회
    fun findByEmail(email: String): String? {
        return redisTemplate.opsForValue().get(email)
    }

    // 리프레시 토큰 삭제
    fun deleteByEmail(email: String) {
        redisTemplate.delete(email)
    }

    // 리프레시 토큰 검증
    fun validateRefreshToken(email: String, token: String): Boolean {
        val storedToken = findByEmail(email)
        return token == storedToken
    }
}
