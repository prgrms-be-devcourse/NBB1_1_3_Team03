package com.sscanner.team.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    //리프레시 토큰 저장
    public void save(String email, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set(email,refreshToken,expirationTime, TimeUnit.MILLISECONDS);
    }

    //저장된 리프레시 토큰 조회
    public String findByEmail(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    //리프레시 토큰 삭제
    public void deleteByEmail(String email) {
        redisTemplate.delete(email);
    }

    //리프레시 토큰 검증
    public boolean validateRefreshToken(String email, String token) {
        String storedToken = findByEmail(email);
        return token.equals(storedToken);
    }

}
