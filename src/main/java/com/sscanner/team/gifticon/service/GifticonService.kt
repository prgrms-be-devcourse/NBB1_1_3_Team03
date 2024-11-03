package com.sscanner.team.gifticon.service

import com.sscanner.team.barcode.entity.Barcode
import com.sscanner.team.barcode.repository.BarcodeRepository
import com.sscanner.team.gifticon.responsedto.GifticonResponseDto
import com.sscanner.team.products.entity.Product
import com.sscanner.team.products.service.ProductImgService
import com.sscanner.team.products.service.ProductService
import org.springframework.stereotype.Service

@Service
class GifticonService(
    private val barcodeRepository: BarcodeRepository,
    private val productService: ProductService,
    private val productImgService: ProductImgService
) {
    fun getGifticonsByUserId(userId: String): List<GifticonResponseDto> {
        val barcodes = barcodeRepository.findAllByUserId(userId)

        val productIds = barcodes.map { it.productId }

        val products = productService.findProductsByIds(productIds)
        val productImages = productImgService.findMainImageUrlsByProductIds(productIds)

        return barcodes.mapNotNull { barcode ->
            toGifticonResponseDto(barcode, products, productImages)
        }
    }

    private fun toGifticonResponseDto(
        barcode: Barcode,
        products: Map<Long, Product>,
        productImages: Map<Long, String>
    ): GifticonResponseDto? {
        val product = products[barcode.productId]
        val representativeProductImgUrl = productImages[barcode.productId]

        return product?.let {
            GifticonResponseDto.of(it, representativeProductImgUrl ?: "", barcode.barcodeUrl)
        }
    }
}
