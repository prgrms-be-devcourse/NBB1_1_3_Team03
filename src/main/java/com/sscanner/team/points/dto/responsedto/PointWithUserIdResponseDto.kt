package com.sscanner.team.points.dto.responsedto

data class PointWithUserIdResponseDto(
    val userId: String,
    val point: Int
) {
    companion object {
        fun of(userId: String, point: Int): PointWithUserIdResponseDto {
            return PointWithUserIdResponseDto(userId, point)
        }
    }
}
