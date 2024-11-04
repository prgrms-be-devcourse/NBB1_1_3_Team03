package com.sscanner.team.auth.jwt

import com.sscanner.team.auth.repository.RedisRefreshTokenRepository
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException

class CustomLogoutFilter(
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RedisRefreshTokenRepository
) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse

        if (!isLogoutRequest(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val refreshToken = getRefreshTokenFromCookies(request.cookies)

        if (!isValidRefreshToken(refreshToken, response)) {
            return
        }

        logout(refreshToken!!, response)
        response.status = HttpServletResponse.SC_OK
    }

    private fun isLogoutRequest(request: HttpServletRequest): Boolean {
        return LOGOUT_URI == request.requestURI && POST_METHOD == request.method
    }

    private fun getRefreshTokenFromCookies(cookies: Array<Cookie>?): String? {
        return cookies?.firstOrNull { it.name == REFRESH_TOKEN }?.value
    }

    private fun isValidRefreshToken(refresh: String?, response: HttpServletResponse): Boolean {
        if (refresh == null) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return false
        }

        try {
            jwtUtil.isExpired(refresh)
        } catch (e: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return false
        }

        if (jwtUtil.getCategory(refresh) != REFRESH_TOKEN) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return false
        }

        // Redis에서 해당 이메일의 토큰이 존재하는지 확인
        val email = jwtUtil.getEmail(refresh)
        val storedToken = refreshRepository.findByEmail(email)

        if (storedToken == null || storedToken != refresh) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return false
        }

        return true
    }

    private fun logout(refresh: String, response: HttpServletResponse) {
        // Redis에서 리프레시 토큰 제거
        val email = jwtUtil.getEmail(refresh)
        refreshRepository.deleteByEmail(email)
        invalidateCookie(response)
    }

    // Refresh 토큰 Cookie 무효화
    private fun invalidateCookie(response: HttpServletResponse) {
        val cookie = Cookie(REFRESH_TOKEN, null)
        cookie.maxAge = 0
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true // HTTPS 통신 시만 전송

        response.addCookie(cookie)
    }

    companion object {
        private const val REFRESH_TOKEN = "refresh"
        private const val LOGOUT_URI = "/logout"
        private const val POST_METHOD = "POST"
    }
}
