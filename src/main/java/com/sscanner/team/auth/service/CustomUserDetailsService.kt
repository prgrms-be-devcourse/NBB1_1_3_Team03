package com.sscanner.team.auth.service

import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.user.repository.UserRepository
import com.sscanner.team.user.responsedto.UserDetailsImpl
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CustomUserDetailsService : UserDetailsService {
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository!!.findByEmail(email)
        if (user.isPresent) {
            return UserDetailsImpl(user.get())
        }
        throw BadRequestException(ExceptionCode.USER_NOT_FOUND)
    }
}
