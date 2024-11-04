package com.sscanner.team.user.requestdto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserPhoneUpdateRequestDto(
        @field:Pattern(
                regexp = "^\\d{11}$",
                message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
        )
        @field:NotBlank(message = "새 핸드폰 번호를 입력해주세요.")
        val newPhone: String,

        @field:Pattern(
                regexp = "^\\d{6}$",
                message = "인증번호는 6자리 숫자만 입력해야 합니다."
        )
        @field:NotBlank(message = "인증번호가 비어있습니다.")
        val smsCode: String
)
