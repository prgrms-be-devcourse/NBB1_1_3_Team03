package com.sscanner.team.points.controller

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.points.dto.requestdto.PointRequestDto
import com.sscanner.team.points.dto.responsedto.PointWithUserIdResponseDto
import com.sscanner.team.points.service.PointService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/points")
class PointController(private val pointService: PointService) {
    @GetMapping("/{userId}")
    fun getUserPoints(@PathVariable userId: String): ApiResponse<PointWithUserIdResponseDto?> {
        val pointWithUserIdResponseDto = pointService.getCachedPoint(userId)
        return ApiResponse.ok(200, pointWithUserIdResponseDto, "사용자 포인트 조회 성공")
    }

    @PostMapping("/add")
    fun addUserPoints(@RequestBody pointRequestDto: PointRequestDto): ApiResponse<PointWithUserIdResponseDto?> {
        val response = pointService.addPoint(pointRequestDto)
        return ApiResponse.ok(201, response, "포인트가 성공적으로 추가되었습니다.")
    }
}
