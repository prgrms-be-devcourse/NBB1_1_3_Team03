package com.sscanner.team.auth.service;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.auth.responsedto.RefreshResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ReissueService {
    ApiResponse<RefreshResponseDto> reissueToken(HttpServletRequest request, HttpServletResponse response);
}
