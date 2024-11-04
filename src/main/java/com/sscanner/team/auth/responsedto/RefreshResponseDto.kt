package com.sscanner.team.auth.responsedto

data class RefreshResponseDto(
    val accessToken: String,
    val refreshToken: String
)
