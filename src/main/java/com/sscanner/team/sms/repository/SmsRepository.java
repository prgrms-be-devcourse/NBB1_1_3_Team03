package com.sscanner.team.sms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsRepository {

    private static final String PREFIX = "sms:"; // 키
    private final StringRedisTemplate stringRedisTemplate;
    private static final int LIMIT_TIME = 60 * 120; // 유효시간 (2분)


    // 인증 정보 저장
    public void createSmsCertification(String phone, String code) {
        stringRedisTemplate.opsForValue()
                .set(PREFIX + phone, code, Duration.ofSeconds(LIMIT_TIME));
    }

    // 인증 정보 조회
    public String getSmsCertification(String phone) {
        return stringRedisTemplate.opsForValue().get(PREFIX+ phone);
    }

    //인증 정보 삭제
    public void deleteSmsCertification(String phone) {
        stringRedisTemplate.delete(PREFIX + phone);
    }

    // 인증 정보 Redis에 존재 확인
    public boolean hasKey(String phone) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(PREFIX + phone)); // Redis에서 해당 키의 존재 여부 확인
    }
}

