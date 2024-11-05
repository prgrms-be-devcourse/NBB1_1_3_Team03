package com.sscanner.team.global.common.response

import lombok.NoArgsConstructor

@NoArgsConstructor
class ApiResponse<T>(val code: Int, val message: String, val data: T?) {

    companion object {
        @JvmStatic
        fun <T> ok(status: Int, data: T, message: String): ApiResponse<T> {
            return ApiResponse(status, message, data)
        }

        @JvmStatic
        fun <T> ok(data: T, message: String): ApiResponse<T> {
            return ApiResponse(200, message, data)
        }

        @JvmStatic
        fun <T> ok(status: Int, message: String): ApiResponse<T> {
            return ApiResponse(status, message, null)
        }

        @JvmStatic
        fun ok(message: String): ApiResponse<*> {
            return ApiResponse<Any?>(200, message, null)
        }

        @JvmStatic
        fun error(status: Int, message: String): ApiResponse<*> {
            return ApiResponse<Any?>(status, message, null)
        }
    }
}
