package com.sscanner.team.trashcan.controller

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto
import com.sscanner.team.trashcan.responseDto.TrashcanWithImgResponseDto
import com.sscanner.team.trashcan.service.TrashcanService

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal


@RestController
@RequestMapping("/api/trashcan")
class TrashcanApiController(private val trashcanService: TrashcanService) {
    @PostMapping
    fun registerTrashcan(
        @RequestPart(value = "data") requestDto: @Valid RegisterTrashcanRequestDto?,
        @RequestPart(value = "file") file: MultipartFile?
    ): ApiResponse<TrashcanWithImgResponseDto> {
        val responseDto = trashcanService.registerTrashcan(requestDto, file)

        return ApiResponse.ok(201, responseDto, "쓰레기통 등록 성공")
    }

    @GetMapping("/{trashcanId}")
    fun getTrashcanInfo(@PathVariable trashcanId: Long?): ApiResponse<TrashcanWithImgResponseDto> {
        val responseDto = trashcanService.getTrashcanInfo(trashcanId)

        return ApiResponse.ok(200, responseDto, "쓰레기통 조회 성공")
    }

    @GetMapping("/getNearByTrashcans")
    fun getNearByTrashcan(
        @RequestParam latitude: @NotNull(message = "위도는 필수입니다.") @DecimalMin(
            value = "-90.0",
            message = "위도는 -90 이상이어야 합니다."
        ) @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.") BigDecimal?,

        @RequestParam longitude: @NotNull(message = "경도는 필수입니다.") @DecimalMin(
            value = "-180.0",
            message = "경도는 -180 이상이어야 합니다."
        ) @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.") BigDecimal?
    ): ApiResponse<List<TrashcanResponseDto>> {
        val responseDtos = trashcanService.getTrashcanByCoordinate(latitude, longitude)

        return ApiResponse.ok(200, responseDtos, "쓰레기통 조회 성공")
    }

    @PutMapping("/{trashcanId}")
    fun updateProductInfo(
        @RequestBody requestDto: @Valid UpdateTrashcanRequestDto?,
        @PathVariable trashcanId: Long?
    ): ApiResponse<TrashcanResponseDto> {
        val responseDto = trashcanService.updateTrashcanInfo(trashcanId, requestDto)
        return ApiResponse.ok(200, responseDto, "쓰레기통 정보 변경 성공")
    }

    @DeleteMapping("/{trashcanId}")
    fun deleteTrashcanInfo(@PathVariable trashcanId: Long?): ApiResponse<*> {
        trashcanService.deleteTrashcanInfo(trashcanId)
        return ApiResponse.ok(200, "쓰레기통 정보 삭제 성공")
    }
}
