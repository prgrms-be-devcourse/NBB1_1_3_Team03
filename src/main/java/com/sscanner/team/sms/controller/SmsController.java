package com.sscanner.team.sms.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.sms.requestdto.SmsRequestDto;
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto;
import com.sscanner.team.sms.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send")
    public ApiResponse<?> SendSMS(@RequestBody @Valid SmsRequestDto smsRequestDto){
        smsService.SendSms(smsRequestDto);
        return new ApiResponse<>(200,"문자를 전송했습니다",null);
    }

    @PostMapping("/verify")
    public ApiResponse<?> verifyCode(@RequestBody @Valid SmsVerifyRequestDto req) {
        boolean verify = smsService.verifyCode(req);
        if (verify) {
            return new ApiResponse<>(200,"인증이 완료되었습니다.",null);
        } else {
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED);
        }
    }
}



