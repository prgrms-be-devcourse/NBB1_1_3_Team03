package com.sscanner.team.auth.constant

import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode

class JwtConstants private constructor() {
    init {
        throw BadRequestException(ExceptionCode.INSTANCE_NOT_ALLOWED)
    }

    companion object {
        const val REFRESH_TOKEN: String = "refresh"
        const val ACCESS_TOKEN: String = "access"
        const val ACCESS_TOKEN_EXPIRATION: Long = 30L * 60 * 1000 // 30분
        const val REFRESH_TOKEN_EXPIRATION: Long = 7L * 24 * 60 * 60 * 1000// 7일
    }
}
