package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User

data class UserJoinResponseDto(
    val userId: String,
    val email: String,
    val nickname: String,
    val phone: String
) {
    companion object {
        fun from(user: User): UserJoinResponseDto {
            return UserJoinResponseDto(
                userId = user.userId ?: "",
                email = user.email ?: "",
                nickname = user.nickname ?: "",
                phone = user.phone ?: ""
            )
        }
    }
}
