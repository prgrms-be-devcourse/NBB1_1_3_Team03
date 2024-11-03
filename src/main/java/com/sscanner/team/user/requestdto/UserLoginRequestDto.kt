package com.sscanner.team.user.requestdto

import jakarta.validation.constraints.Email

@JvmRecord
data class UserLoginRequestDto(
    val email: @Email(message = "유효한 이메일 형식이 아닙니다.") String?,
    val password: String
)
