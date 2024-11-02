package com.sscanner.team.points.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.points.dto.requestdto.PointRequestDto;
import com.sscanner.team.points.dto.responsedto.PointWithUserIdResponseDto;
import com.sscanner.team.points.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    @GetMapping("/{userId}")
    public ApiResponse<PointWithUserIdResponseDto> getUserPoints(@PathVariable String userId) {
        PointWithUserIdResponseDto pointWithUserIdResponseDto = pointService.getCachedPoint(userId);
        return ApiResponse.ok(200, pointWithUserIdResponseDto, "사용자 포인트 조회 성공");
    }

    @PostMapping("/add")
    public ApiResponse<PointWithUserIdResponseDto> addUserPoints(@RequestBody PointRequestDto pointRequestDto) {
        PointWithUserIdResponseDto response = pointService.addPoint(pointRequestDto);
        return ApiResponse.ok(201, response, "포인트가 성공적으로 추가되었습니다.");
    }
}
