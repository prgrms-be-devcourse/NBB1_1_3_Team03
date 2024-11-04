package com.sscanner.team.auth.service

import com.sscanner.team.auth.constant.JwtConstants
import com.sscanner.team.auth.jwt.JWTUtil
import com.sscanner.team.auth.repository.RedisRefreshTokenRepository
import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.auth.responsedto.RefreshResponseDto
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class ReissueServiceImpl(
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RedisRefreshTokenRepository
) : ReissueService {

    override fun reissueToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ApiResponse<RefreshResponseDto> {
        val refresh = getRefreshTokenFromCookies(request)

        validateRefreshCategory(refresh)
        validateRefreshToken(refresh)

        val email = jwtUtil.getEmail(refresh)
        val authority = jwtUtil.getAuthority(refresh)

        // 새 access 토큰 발급
        val newAccess = jwtUtil.createJwt(JwtConstants.ACCESS_TOKEN, email, authority, JwtConstants.ACCESS_TOKEN_EXPIRATION)
        val newRefresh = jwtUtil.createJwt(JwtConstants.REFRESH_TOKEN, email, authority, JwtConstants.REFRESH_TOKEN_EXPIRATION)

        // Redis에 리프레시 토큰 저장 (교체함)
        refreshRepository.save(email, newRefresh, JwtConstants.REFRESH_TOKEN_EXPIRATION)

        response.setHeader(JwtConstants.ACCESS_TOKEN, newAccess)
        response.addCookie(createCookie(JwtConstants.REFRESH_TOKEN, newRefresh))

        val responseDto = RefreshResponseDto(newAccess, newRefresh)
        return ApiResponse.ok(200, responseDto, "토큰 재발급 성공")
    }

    private fun getRefreshTokenFromCookies(request: HttpServletRequest): String {
        val cookies = request.cookies ?: throw BadRequestException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN)
        for (cookie in cookies) {
            if (JwtConstants.REFRESH_TOKEN == cookie.name) {
                return cookie.value
            }
        }
        throw BadRequestException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN)
    }

    private fun validateRefreshToken(refresh: String) {
        try {
            jwtUtil.isExpired(refresh)
        } catch (e: ExpiredJwtException) {
            throw BadRequestException(ExceptionCode.EXPIRED_REFRESH_TOKEN)
        }
    }

    private fun validateRefreshCategory(refresh: String) {
        val category = jwtUtil.getCategory(refresh)
        if (JwtConstants.REFRESH_TOKEN != category) {
            throw BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN)
        }
    }

    private fun createCookie(key: String, value: String): Cookie {
        return Cookie(key, value).apply {
            maxAge = 7 * 24 * 60 * 60
            isHttpOnly = true
            secure = true // HTTPS 통신 시 사용
            path = "/"
        }
    }
}
