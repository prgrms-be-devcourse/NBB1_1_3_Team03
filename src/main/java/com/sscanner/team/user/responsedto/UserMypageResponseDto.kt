package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User

data class UserMypageResponseDto(
    val email: String,
    val nickname: String,
    val phone: String
) {
    companion object {
        fun create(user: User): UserMypageResponseDto {
            return UserMypageResponseDto(
                email = user.email,
                nickname = user.nickname,
                phone = user.phone
            )
        }
    }
}