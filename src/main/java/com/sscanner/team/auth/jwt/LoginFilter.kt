package com.sscanner.team.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.sscanner.team.auth.repository.RedisRefreshTokenRepository
import com.sscanner.team.user.requestdto.UserLoginRequestDto
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RedisRefreshTokenRepository
) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val loginDto: UserLoginRequestDto = try {
            val messageBody = StreamUtils.copyToString(request.inputStream, StandardCharsets.UTF_8)
            ObjectMapper().readValue(messageBody, UserLoginRequestDto::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse login request body", e)
        }

        val authToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        return authenticationManager.authenticate(authToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val email = authentication.name
        val authority = authentication.authorities.first().authority
        val access = jwtUtil.createJwt("access", email, authority, 2000000L)
        val refresh = jwtUtil.createJwt("refresh", email, authority, 86400000L)

        refreshRepository.save(email, refresh, 86400000L)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(email, null, authentication.authorities)

        response.setHeader("access", access)
        response.addCookie(createCookie("refresh", refresh))
        response.status = HttpStatus.OK.value()
    }

    private fun createCookie(key: String, value: String): Cookie {
        return Cookie(key, value).apply {
            maxAge = 7 * 24 * 60 * 60
            path = "/"
            isHttpOnly = true
        }
    }
}
