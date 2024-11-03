package com.sscanner.team.auth.jwt

import com.sscanner.team.user.entity.User
import com.sscanner.team.user.responsedto.UserDetailsImpl
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JWTFilter(private val jwtUtil: JWTUtil) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = authorizationHeader.substring(7)

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(accessToken)
        } catch (e: ExpiredJwtException) {
            response.writer.apply {
                print("만료된 access 토큰")
                flush()
            }
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        // access 토큰인지 확인
        val category = jwtUtil.getCategory(accessToken)

        if (category != "access") {
            response.writer.apply {
                print("유효하지 않은 access 토큰")
                flush()
            }
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        // access 토큰이면 email, authority 가져옴
        val email = jwtUtil.getEmail(accessToken)
        val authority = jwtUtil.getAuthority(accessToken)

        // User 객체 생성 (빌더 대신 생성자를 통한 초기화)
        val user = User(
            userId = "",
            email = email,
            authority = authority,
            password = "", // 비밀번호는 필요한 경우 null 또는 빈 문자열로 설정
            nickname = "", // 필요한 경우 설정
            phone = "" // 필요한 경우 설정
        )

        val customUserDetails = UserDetailsImpl(user)
        val authToken: Authentication =
            UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.authorities)
        SecurityContextHolder.getContext().authentication = authToken

        filterChain.doFilter(request, response)
    }
}
