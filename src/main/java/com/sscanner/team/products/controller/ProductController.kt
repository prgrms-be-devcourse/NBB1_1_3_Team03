package com.sscanner.team.products.controller

import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.products.responsedto.ProductImgResponseDto
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto
import com.sscanner.team.products.service.ProductService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun findAllProducts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "9") size: Int
    ): ApiResponse<Map<String, Any>> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val response = productService.findAllWithImgs(pageable)

        return ApiResponse.ok(200, response, "상품 목록 조회 성공")
    }

    @GetMapping("/{productId}")
    fun findProductById(@PathVariable productId: Long): ApiResponse<ProductWithImgResponseDto> {
        val product = productService.findWithImgById(productId)
        return ApiResponse.ok(200, product, "상품 정보 조회 성공")
    }

    @PostMapping("/{productId}/images")
    fun uploadProductImages(
        @PathVariable productId: Long,
        @RequestParam("images") files: List<MultipartFile>
    ): ApiResponse<List<ProductImgResponseDto>> {
        val response = productService.addImages(productId, files)
        return ApiResponse.ok(200, response, "이미지 등록 성공")
    }
}
