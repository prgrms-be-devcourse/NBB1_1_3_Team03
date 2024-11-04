package com.sscanner.team.user.controller

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.user.requestdto.*
import com.sscanner.team.user.responsedto.*
import com.sscanner.team.user.service.UserServiceImpl
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/users")
class UserController(
    private val userServiceImpl: UserServiceImpl
) {

    // 회원가입 기능
    @PostMapping("/join")
    fun registerUser(@RequestBody @Valid requestDTO: UserJoinRequestDto): ApiResponse<UserJoinResponseDto> {
        val responseDto = userServiceImpl.join(requestDTO)
        return ApiResponse.ok(201, responseDto, "회원가입 성공")
    }

    // 마이페이지 조회
    @GetMapping("/my-page")
    fun mypage(): ApiResponse<UserMypageResponseDto> = userServiceImpl.getMypage()

    // 닉네임 수정
    @PatchMapping("/change-nickname")
    fun updateNickname(@RequestBody @Valid requestDto: UserNicknameUpdateRequestDto): ApiResponse<UserNicknameUpdateResponseDto> {
        val responseDto = userServiceImpl.updateNickname(requestDto.newNickname!!)
        return ApiResponse.ok(200, responseDto, "닉네임 수정 성공")
    }

    // 비밀번호 확인 (폰번호 수정 전 현재 비밀번호 확인 후 접근 가능)
    @PostMapping("/confirm-password")
    fun confirmPassword(@RequestBody password: String): ApiResponse<Void> {
        val isConfirmed = userServiceImpl.confirmPassword(password)
        if (isConfirmed) {
            return ApiResponse.ok(200, null, "비밀번호 확인 성공")
        } else throw BadRequestException(ExceptionCode.CURRENT_PASSWORD_NOT_MATCH)
    }

    // 핸드폰 번호 수정 요청
    @PatchMapping("/change-phone")
    fun updatePhoneNumber(@RequestBody @Valid requestDto: UserPhoneUpdateRequestDto): ApiResponse<UserPhoneUpdateResponseDto> {
        val responseDto = userServiceImpl.updatePhoneNumber(requestDto)
        return ApiResponse.ok(200, responseDto, "핸드폰 번호가 수정되었습니다.")
    }

    // 비밀번호 수정
    @PatchMapping("/change-password")
    fun changePassword(@RequestBody @Valid requestDto: UserPasswordChangeRequestDto): ApiResponse<String> {
        val message = userServiceImpl.changePassword(requestDto)
        return ApiResponse.ok(200, message, "비밀번호 수정 성공")
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    fun deleteUser(): ApiResponse<Void> {
        userServiceImpl.deleteUser()
        return ApiResponse.ok(200, null, "회원 탈퇴 성공")
    }

    // 아이디 찾기
    @PostMapping("/find-id")
    fun findId(@RequestBody @Valid requestDto: UserFindIdRequestDto): ApiResponse<UserFindIdResponseDto> {
        val responseDto = userServiceImpl.findUserId(requestDto)
        return ApiResponse.ok(200, responseDto, "아이디 찾기 성공")
    }

    // 비밀번호 찾기
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody @Valid requestDto: UserResetPasswordRequestDto): ApiResponse<Void> {
        userServiceImpl.resetPassword(requestDto)
        return ApiResponse.ok(200, null, "비밀번호가 성공적으로 변경되었습니다.")
    }
}
