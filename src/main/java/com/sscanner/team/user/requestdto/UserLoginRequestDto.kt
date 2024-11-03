package com.sscanner.team.user.requestdto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class UserLoginRequestDto @JsonCreator constructor(
    @JsonProperty("email")
    @field:NotBlank(message = "이메일이 비어있습니다.")
    val email: String,

    @JsonProperty("password")
    @field:NotBlank(message = "비밀번호가 비어있습니다.")
    val password: String
)
