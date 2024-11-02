package com.sscanner.team.payment.responsedto;

public record PointPaymentResponseDto(
        String userId,
        Integer point
) {
    public static PointPaymentResponseDto of(String userId, Integer point) {
        return new PointPaymentResponseDto(userId, point);
    }
}
