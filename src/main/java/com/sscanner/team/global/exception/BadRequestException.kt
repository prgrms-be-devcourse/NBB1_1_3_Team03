package com.sscanner.team.global.exception

class BadRequestException(exceptionCode: ExceptionCode) : RuntimeException() {
    val code = exceptionCode.code
    override val message: String = exceptionCode.message
}
