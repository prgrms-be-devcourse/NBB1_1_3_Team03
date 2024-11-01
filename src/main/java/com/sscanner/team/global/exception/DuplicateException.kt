package com.sscanner.team.global.exception

class DuplicateException(exceptionCode: ExceptionCode) : RuntimeException() {
    val code = exceptionCode.code
    override val message: String = exceptionCode.message
}
