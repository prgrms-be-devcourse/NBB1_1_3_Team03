package com.sscanner.team.payment.service

import com.sscanner.team.payment.requestdto.PointPaymentRequestDto
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto

interface PaymentService {
    fun processPointPayment(pointPaymentRequestDto: PointPaymentRequestDto): PointPaymentResponseDto
}
