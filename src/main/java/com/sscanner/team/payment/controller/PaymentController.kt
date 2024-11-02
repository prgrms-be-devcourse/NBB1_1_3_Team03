package com.sscanner.team.payment.controller

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.payment.requestdto.PointPaymentRequestDto
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto
import com.sscanner.team.payment.service.PaymentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment")
class PaymentController(
    private val paymentService: PaymentService // 생성자 주입
) {
    @PostMapping
    fun payUserPoints(@RequestBody pointPaymentRequestDto: PointPaymentRequestDto): ApiResponse<PointPaymentResponseDto> {
        val response = paymentService.processPointPayment(pointPaymentRequestDto)
        return ApiResponse.ok(201, response, "포인트가 성공적으로 사용되었습니다.")
    }
}
