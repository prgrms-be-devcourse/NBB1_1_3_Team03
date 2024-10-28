package com.sscanner.team.auth.jwt;

import com.sscanner.team.auth.repository.RedisRefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository refreshRepository;

    private static final String REFRESH_TOKEN = "refresh";
    private static final String LOGOUT_URI = "/logout";
    private static final String POST_METHOD = "POST";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!isLogoutRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = getRefreshTokenFromCookies(request.getCookies());

        if (!isValidRefreshToken(refreshToken, response)) {
            return;
        }

        logout(refreshToken, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    private boolean isLogoutRequest(HttpServletRequest request) {
        return LOGOUT_URI.equals(request.getRequestURI()) && POST_METHOD.equals(request.getMethod());
    }

    private String getRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isValidRefreshToken(String refresh, HttpServletResponse response) {

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        String category = jwtUtil.getCategory(refresh);
        if (!REFRESH_TOKEN.equals(category)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        // Redis에서 해당 이메일의 토큰이 존재하는지 확인
        String email = jwtUtil.getEmail(refresh);
        String storedToken = refreshRepository.findByEmail(email);

        if (storedToken == null || !storedToken.equals(refresh)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        return true;
    }

    private void logout(String refresh, HttpServletResponse response) {
        //레디스에서 리프레시 토큰 제거
        String email = jwtUtil.getEmail(refresh);
        refreshRepository.deleteByEmail(email);
        invalidateCookie(response, REFRESH_TOKEN);
    }

    // Refresh 토큰 Cookie null ,값 0
    private void invalidateCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); //HTTPS 통신 시만 전송

        response.addCookie(cookie);
    }
}
