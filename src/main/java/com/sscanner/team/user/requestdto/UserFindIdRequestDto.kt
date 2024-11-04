package com.sscanner.team.user.requestdto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class UserFindIdRequestDto(
    @field:Pattern(
        regexp = "^\\d{11}$",
        message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
    )
    @field:NotNull(message = "휴대폰 번호를 입력해주세요.")
    val phone: String,

    @field:Pattern(
        regexp = "^\\d{6}$",
        message = "인증번호는 6자리 숫자만 입력해야 합니다."
    )
    @field:NotNull(message = "인증번호를 입력해주세요.")
    val code: String
)
