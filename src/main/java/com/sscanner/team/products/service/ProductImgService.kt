package com.sscanner.team.products.service

import com.sscanner.team.products.entity.ProductImg
import org.springframework.web.multipart.MultipartFile

interface ProductImgService {
    fun findByProductId(productId: Long): List<ProductImg>
    fun findImgsGroupedByProductId(productIds: List<Long>): Map<Long, List<ProductImg>>
    fun findMainImageUrlsByProductIds(productIds: List<Long>): Map<Long, String>
    fun uploadImages(productId: Long, files: List<MultipartFile>): List<ProductImg>
}
