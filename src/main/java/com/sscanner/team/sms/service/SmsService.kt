package com.sscanner.team.sms.service

import com.sscanner.team.sms.requestdto.SmsRequestDto
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto

interface SmsService {
    // 비회원 sms 인증
    fun sendSmsForUnregisteredUser(smsRequestDto: SmsRequestDto)

    // 회원 sms 인증
    fun sendSmsForRegisteredUser(smsRequestDto: SmsRequestDto)

    // 인증 코드를 검증하는 메서드
    fun verifyCode(smsVerifyDto: SmsVerifyRequestDto): Boolean

    // 인증코드 전화번호 검증
    fun isVerify(phoneNum: String, code: String): Boolean
}
