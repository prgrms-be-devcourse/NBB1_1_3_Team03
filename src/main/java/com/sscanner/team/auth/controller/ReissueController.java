package com.sscanner.team.auth.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.user.responsedto.RefreshResponseDto;
import com.sscanner.team.auth.service.ReissueServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class ReissueController {

    private final ReissueServiceImpl reissueServiceImpl;

    @PostMapping("/reissue")
    public ApiResponse<RefreshResponseDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueServiceImpl.reissueToken(request, response);
        return ApiResponse.ok(201, null, "토큰 재발급 성공");
    }
}

