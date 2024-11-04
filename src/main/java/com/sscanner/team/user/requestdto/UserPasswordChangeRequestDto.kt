package com.sscanner.team.user.requestdto

data class UserPasswordChangeRequestDto(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)