package com.sscanner.team.sms.service;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.sms.util.SmsCertificationUtil;
import com.sscanner.team.sms.repository.SmsRepository;
import com.sscanner.team.user.repository.UserRepository;
import com.sscanner.team.sms.requestdto.SmsRequestDto;
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class SmsServiceImpl implements SmsService {

    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsRepository smsRepository;
    private final UserRepository userRepository;

    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void sendSms(SmsRequestDto smsRequestDto) {
        String phoneNum = smsRequestDto.phoneNum();

        if (userRepository.findByPhone(phoneNum).isPresent()) {
            throw new BadRequestException(ExceptionCode.DUPLICATED_PHONE);
        }

        int certificationCode = secureRandom.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 난수
        String codeAsString = Integer.toString(certificationCode);

        // SMS 전송
        smsCertificationUtil.sendSMS(phoneNum, codeAsString);

        // 인증 코드 저장
        smsRepository.createSmsCertification(phoneNum, codeAsString);

    }

    @Override
    public boolean verifyCode(SmsVerifyRequestDto smsVerifyDto) {
        if (isVerify(smsVerifyDto.phoneNum(), smsVerifyDto.code())) {
            smsRepository.deleteSmsCertification(smsVerifyDto.phoneNum());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isVerify(String phoneNum, String code) { // 전화번호에 대한 키 존재 + 인증코드 일치 검증
        return smsRepository.hasKey(phoneNum) &&
                smsRepository.getSmsCertification(phoneNum).equals(code);
    }

}
