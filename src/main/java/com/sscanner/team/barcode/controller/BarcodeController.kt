package com.sscanner.team.barcode.controller

import com.sscanner.team.barcode.responsedto.BarcodeResponseDto
import com.sscanner.team.barcode.service.BarcodeService
import com.sscanner.team.global.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/barcodes")
class BarcodeController(private val barcodeService: BarcodeService) {
    @GetMapping("/{userId}")
    fun getUserBarcodes(@PathVariable userId: String): ApiResponse<List<BarcodeResponseDto>> {
        val barcodes = barcodeService.findBarcodesByUserId(userId)
        return ApiResponse.ok(200, barcodes, "바코드 목록 조회 성공")
    }
}
