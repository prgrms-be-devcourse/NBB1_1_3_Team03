package com.sscanner.team.sms.requestdto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class SmsRequestDto @JsonCreator constructor(
    @JsonProperty("phoneNum")
    @field:Pattern(
        regexp = "^\\d{11}$",
        message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
    )
    @field:NotEmpty(message = "휴대폰 번호를 입력해주세요")
    val phoneNum: String
)
