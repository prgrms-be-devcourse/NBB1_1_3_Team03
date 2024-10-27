package com.sscanner.team.sms.requestdto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record SmsRequestDto(

        @Pattern(
                regexp = "^\\d{11}$",
                message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
        )
        @NotEmpty(message = "휴대폰 번호를 입력해주세요")
        String phoneNum
) {

}