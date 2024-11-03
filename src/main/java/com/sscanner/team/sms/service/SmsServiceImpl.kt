package com.sscanner.team.sms.service

import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.sms.repository.SmsRepository
import com.sscanner.team.sms.requestdto.SmsRequestDto
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto
import com.sscanner.team.sms.util.SmsCertificationUtil
import com.sscanner.team.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class SmsServiceImpl (
    private val smsCertificationUtil: SmsCertificationUtil,
    private val smsRepository: SmsRepository,
    private val userRepository: UserRepository
) : SmsService {

    companion object {
        private val secureRandom = SecureRandom()
    }

    override fun sendSmsForUnregisteredUser(smsRequestDto: SmsRequestDto) {
        val phoneNum = smsRequestDto.phoneNum

        if (userRepository.findByPhone(phoneNum).isPresent) {
            throw BadRequestException(ExceptionCode.DUPLICATED_PHONE)
        }

        val certificationCode = secureRandom.nextInt(900000) + 100000 // 100000 ~ 999999 범위의 난수
        val codeAsString = certificationCode.toString()

        // SMS 전송
        smsCertificationUtil.sendSMS(phoneNum, codeAsString)

        // 인증 코드 저장
        smsRepository.createSmsCertification(phoneNum, codeAsString)
    }

    // 가입된 사용자에게 SMS 전송
    override fun sendSmsForRegisteredUser(smsRequestDto: SmsRequestDto) {
        val phoneNum = smsRequestDto.phoneNum

        if (userRepository.findByPhone(phoneNum).isEmpty) {
            throw BadRequestException(ExceptionCode.USER_NOT_FOUND_BY_PHONE)
        }

        val certificationCode = secureRandom.nextInt(900000) + 100000 // 100000 ~ 999999 범위의 난수
        val codeAsString = certificationCode.toString()

        // SMS 전송
        smsCertificationUtil.sendSMS(phoneNum, codeAsString)

        // 인증 코드 저장
        smsRepository.createSmsCertification(phoneNum, codeAsString)
    }

    override fun verifyCode(smsVerifyDto: SmsVerifyRequestDto): Boolean {
        return if (isVerify(smsVerifyDto.phoneNum, smsVerifyDto.code)) {
            smsRepository.deleteSmsCertification(smsVerifyDto.phoneNum)
            true
        } else {
            false
        }
    }

    override fun isVerify(phoneNum: String, code: String): Boolean { // 전화번호에 대한 키 존재 + 인증코드 일치 검증
        return smsRepository.hasKey(phoneNum) &&
                smsRepository.getSmsCertification(phoneNum) == code
    }
}
