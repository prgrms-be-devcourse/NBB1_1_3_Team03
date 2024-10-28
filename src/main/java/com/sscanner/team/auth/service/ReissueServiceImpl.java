package com.sscanner.team.auth.service;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.auth.jwt.JWTUtil;
import com.sscanner.team.auth.repository.RedisRefreshTokenRepository;
import com.sscanner.team.user.responsedto.RefreshResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReissueServiceImpl implements ReissueService {
    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository refreshRepository;

    private static final String REFRESH_TOKEN = "refresh";
    private static final String ACCESS_TOKEN = "access";
    private static final long ACCESS_TOKEN_EXPIRATION = 30L * 60;
    private static final long REFRESH_TOKEN_EXPIRATION =  7L * 24 * 60 * 60;

    @Override
    public ApiResponse<RefreshResponseDto> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = getRefreshTokenFromCookies(request);

        validateRefreshCategory(refresh);
        validateRefreshToken(refresh);

        String email = jwtUtil.getEmail(refresh);
        String authority = jwtUtil.getAuthority(refresh);

        // 새 access 토큰 발급
        String newAccess = jwtUtil.createJwt(ACCESS_TOKEN, email, authority, ACCESS_TOKEN_EXPIRATION);
        String newRefresh = jwtUtil.createJwt(REFRESH_TOKEN, email, authority, REFRESH_TOKEN_EXPIRATION);

       // 레디스에 리프레시 토큰 저장 (교체함)
        refreshRepository.save(email, newRefresh, REFRESH_TOKEN_EXPIRATION);

        response.setHeader(ACCESS_TOKEN, newAccess);
        response.addCookie(createCookie(REFRESH_TOKEN, newRefresh));

        RefreshResponseDto responseDto = RefreshResponseDto.of(newAccess, newRefresh);
        return ApiResponse.ok(200, responseDto, "토큰 재발급 성공");
    }

    private  String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new BadRequestException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN);
    }

    private void validateRefreshToken(String refresh) {
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ExceptionCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    private void validateRefreshCategory(String refresh) {
        String category = jwtUtil.getCategory(refresh);
        if (!REFRESH_TOKEN.equals(category)) {
            throw new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // https 통신 진행 시 사용
        cookie.setPath("/");
        return cookie;
    }
}
