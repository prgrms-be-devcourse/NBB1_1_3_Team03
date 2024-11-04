package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User

data class UserPhoneUpdateResponseDto(
    val userId: String?,
    val newPhone: String
) {
    companion object {
        fun from(user: User): UserPhoneUpdateResponseDto {
            return UserPhoneUpdateResponseDto(
                userId = user.userId,
                newPhone = user.phone
            )
        }
    }
}