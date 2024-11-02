package com.sscanner.team.payment.responsedto

data class PointPaymentResponseDto(
    val userId: String,
    val point: Int
) {
    companion object {
        fun of(userId: String, point: Int): PointPaymentResponseDto {
            return PointPaymentResponseDto(userId, point)
        }
    }
}
