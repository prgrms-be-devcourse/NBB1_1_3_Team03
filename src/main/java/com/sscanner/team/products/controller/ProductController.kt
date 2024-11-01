package com.sscanner.team.products.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.products.responsedto.ProductImgResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Map<String, Object>> findAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Map<String, Object> response = productService.findAllWithImgs(pageable);

        return ApiResponse.ok(200, response, "상품 목록 조회 성공");
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductWithImgResponseDto> findProductById(@PathVariable Long productId) {
        ProductWithImgResponseDto product = productService.findWithImgById(productId);
        return ApiResponse.ok(200, product, "상품 정보 조회 성공");
    }

    @PostMapping("/{productId}/images")
    public ApiResponse<List<ProductImgResponseDto>> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> files
    ) {
        List<ProductImgResponseDto> response = productService.addImages(productId, files);
        return ApiResponse.ok(200, response, "이미지 등록 성공");
    }
}
