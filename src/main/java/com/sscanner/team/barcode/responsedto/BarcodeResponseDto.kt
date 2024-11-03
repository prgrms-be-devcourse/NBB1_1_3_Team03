package com.sscanner.team.barcode.responsedto

import com.sscanner.team.barcode.entity.Barcode

data class BarcodeResponseDto(
    val barcodeId: Long?,
    val barcodeUrl: String,
    val productId: Long
) {
    companion object {
        fun from(barcode: Barcode): BarcodeResponseDto {
            return BarcodeResponseDto(
                barcode.id,
                barcode.barcodeUrl,
                barcode.productId
            )
        }
    }
}
