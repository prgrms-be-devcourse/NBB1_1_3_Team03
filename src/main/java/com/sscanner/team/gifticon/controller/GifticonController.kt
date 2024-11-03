package com.sscanner.team.gifticon.controller;

import com.sscanner.team.gifticon.responsedto.GifticonResponseDto;
import com.sscanner.team.gifticon.service.GifticonService;
import com.sscanner.team.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gifticons")
public class GifticonController {

    private final GifticonService gifticonService;

    /**
     * 사용자별 기프티콘 목록 조회 API (대표 상품 이미지를 포함)
     * @param userId 사용자 ID
     * @return 기프티콘 목록
     */
    @GetMapping("/{userId}")
    public ApiResponse<List<GifticonResponseDto>> getUserGifticons(@PathVariable String userId) {
        List<GifticonResponseDto> gifticons = gifticonService.getGifticonsByUserId(userId);
        return ApiResponse.ok(200, gifticons, "기프티콘 목록 조회 성공");
    }
}
