package com.sscanner.team.products.repository

import com.sscanner.team.products.entity.ProductImg
import org.springframework.data.jpa.repository.JpaRepository

interface ProductImgRepository : JpaRepository<ProductImg, Long> {
    fun findAllByProductId(productId: Long): List<ProductImg>
    fun findAllByProductIdIn(productIds: List<Long>): List<ProductImg>
}
