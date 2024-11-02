package com.sscanner.team.payment.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.payment.requestdto.PointPaymentRequestDto;
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto;
import com.sscanner.team.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ApiResponse<PointPaymentResponseDto> payUserPoints(@RequestBody PointPaymentRequestDto pointPaymentRequestDto) {
        PointPaymentResponseDto response = paymentService.processPointPayment(pointPaymentRequestDto);
        return ApiResponse.ok(201, response, "포인트가 성공적으로 사용되었습니다.");
    }
}
