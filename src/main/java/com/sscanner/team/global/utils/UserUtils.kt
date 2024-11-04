package com.sscanner.team.global.utils

import com.sscanner.team.global.exception.DuplicateException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.user.entity.User
import com.sscanner.team.user.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserUtils(
    private val userRepository: UserRepository
) {
    // 현재 로그인한 유저 정보 가져옴
    val user: User
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            val email = authentication.name
            return userRepository.findByEmail(email)
                .orElseThrow {
                    DuplicateException(ExceptionCode.USER_NOT_FOUND)
                }
        }
}
