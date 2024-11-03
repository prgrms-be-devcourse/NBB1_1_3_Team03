package com.sscanner.team.sms.controller

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.sms.requestdto.SmsRequestDto
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto
import com.sscanner.team.sms.service.SmsServiceImpl
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sms")
class SmsController (
    private val smsServiceImpl: SmsServiceImpl
) {
    @PostMapping("/send-for-unregistered")
    fun sendSmsForUnregisteredUser(@RequestBody @Valid smsRequestDto: SmsRequestDto): ApiResponse<Void?> {
        smsServiceImpl.sendSmsForUnregisteredUser(smsRequestDto)
        return ApiResponse(200, "문자를 전송했습니다", null)
    }

    @PostMapping("/send-for-registered")
    fun sendSmsForRegisteredUser(@RequestBody @Valid smsRequestDto: SmsRequestDto): ApiResponse<Void?> {
        smsServiceImpl.sendSmsForRegisteredUser(smsRequestDto)
        return ApiResponse(200, "문자를 전송했습니다", null)
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestBody @Valid req: SmsVerifyRequestDto): ApiResponse<Void?> {
        val verify = smsServiceImpl.verifyCode(req)
        if (verify) {
            return ApiResponse(200, "인증이 완료되었습니다.", null)
        } else {
            throw BadRequestException(ExceptionCode.UNAUTHORIZED)
        }
    }
}