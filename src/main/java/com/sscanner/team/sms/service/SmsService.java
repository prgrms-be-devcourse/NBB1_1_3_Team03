package com.sscanner.team.sms.service;

import com.sscanner.team.sms.requestdto.SmsRequestDto;
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto;

public interface SmsService {

    //sms 전송
    void sendSms(SmsRequestDto smsRequestDto);

    // 인증 코드를 검증하는 메서드
    boolean verifyCode(SmsVerifyRequestDto smsVerifyDto);

    // 인증코드 전화번호 검증
    boolean isVerify(String phoneNum, String code);
}
