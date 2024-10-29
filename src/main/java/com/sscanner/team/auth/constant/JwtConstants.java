package com.sscanner.team.auth.constant;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;

public class JwtConstants {
    public static final String REFRESH_TOKEN = "refresh";
    public static final String ACCESS_TOKEN = "access";
    public static final long ACCESS_TOKEN_EXPIRATION = 30L * 60;  // 30분
    public static final long REFRESH_TOKEN_EXPIRATION = 7L * 24 * 60 * 60;  // 7일

    private JwtConstants() {
        throw new BadRequestException(ExceptionCode.INSTANCE_NOT_ALLOWED);
    }
}
