package com.sscanner.team.sms.requestdto;

import jakarta.validation.constraints.NotNull;

public record SmsVerifyRequestDto (

        @NotNull(message = "휴대폰 번호를 입력해주세요.")
        String phoneNum,
        @NotNull(message = "인증번호를 입력해주세요.")
        String code
){
}
