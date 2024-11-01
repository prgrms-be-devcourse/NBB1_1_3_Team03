package com.sscanner.team.products.service

import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.products.entity.Product
import com.sscanner.team.products.entity.ProductImg
import com.sscanner.team.products.repository.ProductRepository
import com.sscanner.team.products.responsedto.ProductImgResponseDto
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productImgService: ProductImgService
) : ProductService {

    override fun findAllWithImgs(pageable: Pageable): Map<String, Any> {
        val products = productRepository.findAll(pageable)
        val productIds = products.mapNotNull { it.id }
        val productImgsMap = productImgService.findImgsGroupedByProductId(productIds)

        val productWithImgDtos = products.map { product ->
            val productImgs = productImgsMap[product.id] ?: emptyList()
            toProductWithImgDto(product, productImgs)
        }.toList()

        return createResponse(productWithImgDtos, products)
    }


    override fun findWithImgById(productId: Long): ProductWithImgResponseDto {
        val product = findById(productId)
        val productImgs = productImgService.findByProductId(productId)
        return toProductWithImgDto(product, productImgs)
    }

    @Transactional
    override fun addImages(productId: Long, files: List<MultipartFile>): List<ProductImgResponseDto> {
        return productImgService.uploadImages(productId, files).map {
            ProductImgResponseDto.from(it)
        }
    }

    override fun findById(productId: Long): Product {
        return productRepository.findById(productId)
            .orElseThrow { BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID) }
    }

    override fun findProductsByIds(productIds: List<Long>): Map<Long, Product> {
        return productRepository.findAllById(productIds).associateBy { it.id!! }
    }

    private fun toProductWithImgDto(product: Product, productImgs: List<ProductImg>): ProductWithImgResponseDto {
        val imgUrls = productImgs.map { it.url }
        return ProductWithImgResponseDto.from(product, imgUrls)
    }

    private fun createResponse(
        productWithImgDtos: List<ProductWithImgResponseDto>,
        products: Page<Product>
    ): Map<String, Any> {
        return mapOf(
            "products" to productWithImgDtos,
            "currentPage" to (products.number + 1),
            "totalItems" to products.totalElements,
            "totalPages" to products.totalPages
        )
    }
}
