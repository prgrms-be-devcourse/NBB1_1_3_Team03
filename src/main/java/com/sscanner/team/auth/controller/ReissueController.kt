package com.sscanner.team.auth.controller

import com.sscanner.team.auth.service.ReissueServiceImpl
import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.auth.responsedto.RefreshResponseDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReissueController(
    private val reissueServiceImpl: ReissueServiceImpl
) {
    @PostMapping("/reissue")
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ApiResponse<RefreshResponseDto> {
        return reissueServiceImpl.reissueToken(request, response)
    }
}
