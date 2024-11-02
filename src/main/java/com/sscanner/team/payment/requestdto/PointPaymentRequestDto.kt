package com.sscanner.team.payment.requestdto;

public record PointPaymentRequestDto(
        String userId,
        Long productId
) {
}
