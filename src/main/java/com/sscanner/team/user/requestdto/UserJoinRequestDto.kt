package com.sscanner.team.user.requestdto

import com.sscanner.team.user.entity.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserJoinRequestDto(
    @field:Email(message = "유효한 이메일 형식이 아닙니다.")
    @field:NotBlank(message = "이메일이 비어있습니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호가 비어있습니다.")
    val password: String,

    @field:NotBlank(message = "비밀번호 확인이 비어있습니다.")
    val passwordCheck: String,

    @field:NotBlank(message = "닉네임이 비어있습니다.")
    val nickname: String,

    @field:Pattern(
        regexp = "^\\d{11}$",
        message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
    )
    @field:NotBlank(message = "전화번호가 비어있습니다.")
    val phone: String,

    @field:Pattern(
        regexp = "^\\d{6}$",
        message = "인증 번호는 6자리 숫자만 입력해야 합니다."
    )
    @field:NotBlank(message = "인증번호가 비어있습니다.")
    val smsCode: String
) {
    fun toEntity(encodedPassword: String): User {
        return User(
            email = email,
            password = encodedPassword,
            nickname = nickname,
            phone = phone,
            authority = "ROLE_USER"
        )
    }
}
