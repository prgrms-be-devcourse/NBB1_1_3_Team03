package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User

@JvmRecord
data class UserJoinResponseDto(
    val userId: String,
    val email: String,
    val nickname: String,
    val phone: String
) {
    companion object {
        fun from(user: User): UserJoinResponseDto {
            return UserJoinResponseDto(
                user.getUserId(),
                user.email,
                user.nickname,
                user.phone
            )
        }
    }
}