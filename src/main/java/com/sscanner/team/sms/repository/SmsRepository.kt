package com.sscanner.team.sms.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class SmsRepository (
    private val stringRedisTemplate: StringRedisTemplate
){
    // 인증 정보 저장
    fun createSmsCertification(phone: String, code: String) {
        stringRedisTemplate.opsForValue()
            .set(PREFIX + phone, code, Duration.ofSeconds(LIMIT_TIME))
    }

    // 인증 정보 조회
    fun getSmsCertification(phone: String): String? {
        return stringRedisTemplate.opsForValue().get(PREFIX + phone)
    }

    //인증 정보 삭제
    fun deleteSmsCertification(phone: String) {
        stringRedisTemplate.delete(PREFIX + phone)
    }

    // 인증 정보 Redis에 존재 확인
    fun hasKey(phone: String): Boolean {
        return stringRedisTemplate.hasKey(PREFIX + phone) == true // Redis에서 해당 키의 존재 여부 확인
    }

    companion object {
        private const val PREFIX = "sms:" // 키
        private const val LIMIT_TIME = 60 * 2L // 유효시간 (2분)
    }
}