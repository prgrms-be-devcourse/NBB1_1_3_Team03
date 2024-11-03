package com.sscanner.team.gifticon.responsedto

import com.sscanner.team.products.entity.Product

data class GifticonResponseDto(
    val productName: String,
    val productImageUrl: String,
    val barcodeImageUrl: String
) {
    companion object {
        fun of(product: Product, productImageUrl: String = "", barcodeImageUrl: String = ""): GifticonResponseDto {
            return GifticonResponseDto(
                product.name,
                productImageUrl.ifEmpty { "defaultProductImageUrl" }, // 필요에 따라 기본 URL을 설정
                barcodeImageUrl.ifEmpty { "defaultBarcodeImageUrl" } // 필요에 따라 기본 URL을 설정
            )
        }
    }
}
