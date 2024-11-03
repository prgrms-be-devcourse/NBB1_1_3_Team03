package com.sscanner.team.sms.util

import jakarta.annotation.PostConstruct
import net.nurigo.sdk.NurigoApp.initialize
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SmsCertificationUtil {
    @Value("\${coolsms.api.key}")
    private lateinit var apiKey: String

    @Value("\${coolsms.api.secret}")
    private lateinit var apiSecret: String

    @Value("\${coolsms.api.number}")
    private lateinit var fromNumber: String

    private lateinit var messageService: DefaultMessageService

    @PostConstruct // 의존성 주입이 완료된 후 초기화 수행
    fun init() {
        messageService = initialize(apiKey, apiSecret, "https://api.coolsms.co.kr")
    }

    // 단일 메시지 발송
    fun sendSMS(to: String, certificationCode: String) {
        val message = Message()
        message.from = fromNumber
        message.to = to
        message.text = "본인확인 인증번호는 $certificationCode 입니다."

        messageService.sendOne(SingleMessageSendingRequest(message))
    }
}