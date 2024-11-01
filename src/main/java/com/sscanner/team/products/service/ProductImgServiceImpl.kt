package com.sscanner.team.products.service

import com.sscanner.team.global.common.service.ImageService
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.products.entity.ProductImg
import com.sscanner.team.products.repository.ProductImgRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ProductImgServiceImpl(
    private val productImgRepository: ProductImgRepository,
    private val imageService: ImageService
) : ProductImgService {

    override fun findByProductId(productId: Long): List<ProductImg> {
        return productImgRepository.findAllByProductId(productId)
    }

    override fun findImgsGroupedByProductId(productIds: List<Long>): Map<Long, List<ProductImg>> {
        val productImgs = productImgRepository.findAllByProductIdIn(productIds)
        return productImgs.groupBy { it.productId }
    }

    override fun findMainImageUrlsByProductIds(productIds: List<Long>): Map<Long, String> {
        val productImgs = productImgRepository.findAllByProductIdIn(productIds)
        return productImgs.associate { it.productId to it.url }
    }

    @Transactional
    override fun uploadImages(productId: Long, files: List<MultipartFile>): List<ProductImg> {
        return try {
            files.map { file ->
                val imgUrl = makeImgUrl(file)
                saveProductImg(productId, imgUrl)
            }
        } catch (e: Exception) {
            throw BadRequestException(ExceptionCode.FILE_UPLOAD_FAIL)
        }
    }

    private fun makeImgUrl(file: MultipartFile): String {
        return imageService.makeImgUrl(file)
    }

    private fun saveProductImg(productId: Long, imgUrl: String): ProductImg {
        val productImg = ProductImg.create(productId, imgUrl)
        return productImgRepository.save(productImg)
    }
}
