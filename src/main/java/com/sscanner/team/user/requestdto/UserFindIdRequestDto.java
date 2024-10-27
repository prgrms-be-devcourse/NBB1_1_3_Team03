package com.sscanner.team.user.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserFindIdRequestDto(

        @Pattern(
                regexp = "^\\d{11}$",
                message = "핸드폰 번호는 11자리 숫자만 입력해야 합니다."
        )
        @NotNull(message = "휴대폰 번호를 입력해주세요.")
        String phone,

        @Pattern(
                regexp = "^\\d{6}$",
                message = "인증번호는 6자리 숫자만 입력해야 합니다."
        )
        @NotNull(message =  "인증번호를 입력해주세요.")
        String code
) {
}
