package com.sscanner.team.user.service

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.user.requestdto.*
import com.sscanner.team.user.responsedto.*

interface UserService {
    fun join(req: UserJoinRequestDto?): UserJoinResponseDto?

    val mypage: ApiResponse<UserMypageResponseDto?>?

    fun confirmPassword(password: String?): Boolean

    fun updatePhoneNumber(req: UserPhoneUpdateRequestDto?): UserPhoneUpdateResponseDto?

    fun updateNickname(newNickname: String?): UserNicknameUpdateResponseDto?

    fun changePassword(requestDto: UserPasswordChangeRequestDto?): String?

    fun deleteUser()

    fun findUserId(requestDto: UserFindIdRequestDto?): UserFindIdResponseDto?

    fun resetPassword(requestDto: UserResetPasswordRequestDto?)
}
