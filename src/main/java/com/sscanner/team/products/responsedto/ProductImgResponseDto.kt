package com.sscanner.team.products.responsedto

import com.sscanner.team.products.entity.ProductImg
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ProductImgResponseDto(
    @field:NotNull
    val productId: Long,

    @field:NotEmpty
    val url: String
) {
    companion object {
        fun from(productImg: ProductImg): ProductImgResponseDto {
            return ProductImgResponseDto(
                productId = productImg.productId,
                url = productImg.url
            )
        }
    }
}
