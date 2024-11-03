package com.sscanner.team.user.requestdto

import com.sscanner.team.user.entity.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@JvmRecord
data class UserJoinRequestDto(
    val email: @Email(message = "유효한 이메일 형식이 아닙니다.") @NotBlank(message = "이메일이 비어있습니다.") String?,

    val password: @NotBlank(message = "비밀번호가 비어있습니다.") String?,

    val passwordCheck: @NotBlank(message = "비밀번호 확인이 비어있습니다.") String?,

    val nickname: @NotBlank(message = "닉네임이 비어있습니다.") String?,

    val phone: @Pattern(
        regexp = "^\\d{11}$",
        message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
    ) @NotBlank(message = "전화번호가 비어있습니다.") String?,


    val smsCode: @Pattern(
        regexp = "^\\d{6}$",
        message = "인증 번호는 6자리 숫자만 입력해야 합니다."
    ) @NotBlank(message = "인증번호가 비어있습니다.") String?


) {
    fun toEntity(encodedPassword: String?): User {
        return User.builder()
            .email(email)
            .password(encodedPassword)
            .nickname(nickname)
            .phone(phone)
            .authority("ROLE_USER")
            .build()
    }
}
