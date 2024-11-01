package com.sscanner.team.products.responsedto

import com.sscanner.team.products.entity.Product

data class ProductWithImgResponseDto(
    val productId: Long,
    val productName: String,
    val price: Int,
    val imgUrls: List<String>
) {
    companion object {
        fun from(product: Product, imgUrls: List<String>): ProductWithImgResponseDto {
            return ProductWithImgResponseDto(
                productId = product.id ?: throw IllegalArgumentException("Product ID cannot be null"),
                productName = product.name,
                price = product.price,
                imgUrls = imgUrls
            )
        }
    }
}
