package com.sscanner.team.user.service

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.DuplicateException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.global.utils.UserUtils
import com.sscanner.team.sms.requestdto.SmsVerifyRequestDto
import com.sscanner.team.sms.service.SmsService
import com.sscanner.team.user.entity.User
import com.sscanner.team.user.repository.UserRepository
import com.sscanner.team.user.requestdto.*
import com.sscanner.team.user.responsedto.*
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val smsService: SmsService,
    private val userUtils: UserUtils
) : UserService {

    // 이메일 중복 체크
    private fun checkDuplicatedEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw DuplicateException(ExceptionCode.DUPLICATED_EMAIL)
        }
    }

    // 닉네임 중복 체크
    private fun checkDuplicatedNickname(nickname: String) {
        if (userRepository.existsByNickname(nickname)) {
            throw DuplicateException(ExceptionCode.DUPLICATED_NICKNAME)
        }
    }

    // 핸드폰 중복 체크
    private fun checkDuplicatedPhone(phone: String) {
        if (userRepository.existsByPhone(phone)) {
            throw DuplicateException(ExceptionCode.DUPLICATED_PHONE)
        }
    }

    // 비밀번호 확인용 메서드 (비번, 비번 확인 같은지)
    private fun confirmPassword(password: String, passwordCheck: String) {
        if (password != passwordCheck) {
            throw BadRequestException(ExceptionCode.PASSWORD_NOT_MATCH)
        }
    }

    // 비밀번호 수정 시 검증
    private fun validatePasswordChange(requestDto: UserPasswordChangeRequestDto, user: User) {
        if (!passwordEncoder.matches(requestDto.currentPassword, user.password)) {
            throw BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH)
        }
        confirmPassword(requestDto.newPassword, requestDto.confirmNewPassword)
    }

    // 핸드폰 인증 코드 검증
    private fun verifyPhoneCode(phone: String, code: String) {
        if (!smsService.verifyCode(SmsVerifyRequestDto(phone, code))) {
            throw BadRequestException(ExceptionCode.PHONE_VERIFICATION_FAILED)
        }
    }

    // 회원가입
    override fun join(req: UserJoinRequestDto): UserJoinResponseDto {
        checkDuplicatedEmail(req.email)
        checkDuplicatedNickname(req.nickname)
        checkDuplicatedPhone(req.phone)

        verifyPhoneCode(req.phone, req.smsCode)
        confirmPassword(req.password, req.passwordCheck)

        val userEntity = req.toEntity(passwordEncoder.encode(req.password))
        userRepository.save(userEntity)

        return UserJoinResponseDto.from(userEntity)
    }

    // 마이페이지 조회
    override fun getMypage(): ApiResponse<UserMypageResponseDto> {
        val user = userUtils.user
        val responseDto = UserMypageResponseDto.create(user)
        return ApiResponse.ok(responseDto, "마이페이지 조회")
    }

    // 비밀번호 확인 (현재 비밀번호 확인)
    override fun confirmPassword(password: String): Boolean {
        val user = userUtils.user

        if (!passwordEncoder.matches(password, user.password)) {
            throw BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH)
        }
        return true
    }

    // 핸드폰 번호 수정
    @Transactional
    override fun updatePhoneNumber(req: UserPhoneUpdateRequestDto): UserPhoneUpdateResponseDto {
        val user = userUtils.user

        if (user.isPhoneEqual(req.newPhone)) {
            throw BadRequestException(ExceptionCode.SAME_PHONE_NUMBER)
        }

        checkDuplicatedPhone(req.newPhone)
        verifyPhoneCode(req.newPhone, req.smsCode)

        user.changePhone(req.newPhone)
        return UserPhoneUpdateResponseDto.from(user)
    }

    // 닉네임 수정
    @Transactional
    override fun updateNickname(newNickname: String): UserNicknameUpdateResponseDto {
        val user = userUtils.user

        if (!user.isNicknameEqual(newNickname)) {
            checkDuplicatedNickname(newNickname)
            user.changeNickname(newNickname)
        }
        return UserNicknameUpdateResponseDto.from(user)
    }

    // 비밀번호 수정
    @Transactional
    override fun changePassword(requestDto: UserPasswordChangeRequestDto): String {
        val user = userUtils.user

        validatePasswordChange(requestDto, user)

        user.changePassword(passwordEncoder.encode(requestDto.newPassword))
        userRepository.save(user)

        return "비밀번호가 성공적으로 변경되었습니다."
    }

    // 회원 탈퇴
    @Transactional
    override fun deleteUser() {
        val user = userUtils.user
        userRepository.delete(user)
    }

    // 아이디 찾기
    override fun findUserId(requestDto: UserFindIdRequestDto): UserFindIdResponseDto {
        val user = userRepository.findByPhone(requestDto.phone)
            .orElseThrow {
                BadRequestException(ExceptionCode.USER_NOT_FOUND_BY_PHONE)
            }

        verifyPhoneCode(requestDto.phone, requestDto.code)
        return UserFindIdResponseDto(user.email)
    }

    // 비밀번호 찾기 (리셋)
    @Transactional
    override fun resetPassword(requestDto: UserResetPasswordRequestDto) {
        val user = userRepository.findByEmailAndPhone(requestDto.email, requestDto.phone)
            .orElseThrow { NoSuchElementException("아이디 또는 핸드폰 번호를 다시 확인해 주세요.") }

        verifyPhoneCode(requestDto.phone, requestDto.code)

        user.changePassword(passwordEncoder.encode(requestDto.newPassword))
        userRepository.save(user)
    }
}
