package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User

data class UserNicknameUpdateResponseDto(
    val userId: String?,
    val newNickname: String
) {
    companion object {
        fun from(user: User): UserNicknameUpdateResponseDto {
            return UserNicknameUpdateResponseDto(
                userId = user.userId,
                newNickname = user.nickname
            )
        }
    }
}