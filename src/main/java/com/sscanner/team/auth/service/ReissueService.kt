package com.sscanner.team.auth.service

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.auth.responsedto.RefreshResponseDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface ReissueService {
    fun reissueToken(request: HttpServletRequest, response: HttpServletResponse): ApiResponse<RefreshResponseDto>
}
