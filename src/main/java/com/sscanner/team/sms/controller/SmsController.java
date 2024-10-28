package com.sscanner.team.sms.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.sms.requestdto.SmsRequestDto;
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto;
import com.sscanner.team.sms.service.SmsServiceImpl;
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

    private final SmsServiceImpl smsServiceImpl;

    @PostMapping("/send")
    public ApiResponse<Void> sendSMS(@RequestBody @Valid SmsRequestDto smsRequestDto){
        smsServiceImpl.sendSms(smsRequestDto);
        return new ApiResponse<>(200,"문자를 전송했습니다",null);
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verifyCode(@RequestBody @Valid SmsVerifyRequestDto req) {
        boolean verify = smsServiceImpl.verifyCode(req);
        if (verify) {
            return new ApiResponse<>(200,"인증이 완료되었습니다.",null);
        } else {
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED);
        }
    }
}



