package com.sscanner.team.user.requestdto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class UserNicknameUpdateRequestDto @JsonCreator constructor(
    @JsonProperty("newNickname")
    @field:NotBlank(message = "새 닉네임을 입력해주세요.")
    val newNickname: String
)