package com.sscanner.team.products.service

import com.sscanner.team.products.entity.Product
import com.sscanner.team.products.responsedto.ProductImgResponseDto
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile

interface ProductService {
    fun findAllWithImgs(pageable: Pageable): Map<String, Any>
    fun findWithImgById(productId: Long): ProductWithImgResponseDto
    fun addImages(productId: Long, files: List<MultipartFile>): List<ProductImgResponseDto>
    fun findById(productId: Long): Product
    fun findProductsByIds(productIds: List<Long>): Map<Long, Product>
}
