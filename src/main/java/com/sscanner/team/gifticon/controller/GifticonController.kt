package com.sscanner.team.gifticon.controller

import com.sscanner.team.gifticon.responsedto.GifticonResponseDto
import com.sscanner.team.gifticon.service.GifticonService
import com.sscanner.team.global.common.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/gifticons")
class GifticonController(private val gifticonService: GifticonService) {
    @GetMapping("/{userId}")
    fun getUserGifticons(@PathVariable userId: String): ApiResponse<List<GifticonResponseDto>> {
        val gifticons = gifticonService.getGifticonsByUserId(userId)
        return ApiResponse.ok(gifticons, "기프티콘 목록 조회 성공")
    }
}
