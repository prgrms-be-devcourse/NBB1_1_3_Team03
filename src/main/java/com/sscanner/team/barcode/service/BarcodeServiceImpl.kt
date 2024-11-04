package com.sscanner.team.barcode.service

import com.sscanner.team.barcode.common.BarcodeConstants
import com.sscanner.team.barcode.entity.Barcode
import com.sscanner.team.barcode.repository.BarcodeRepository
import com.sscanner.team.barcode.responsedto.BarcodeResponseDto
import com.sscanner.team.global.common.service.ImageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BarcodeServiceImpl(
    private val barcodeRepository: BarcodeRepository,
    private val imageService: ImageService,
    private val barcodeGenerator: BarcodeGenerator
) : BarcodeService {

    @Transactional
    override fun createAndSaveBarcode(userId: String, productId: Long): Barcode {
        val barcodeText = generateBarcodeText(userId, productId)
        val barcodeImage = generateBarcodeImage(barcodeText)
        val barcodeUrl = uploadBarcodeImage(barcodeImage)

        return saveBarcode(userId, productId, barcodeUrl)
    }

    override fun findBarcodesByUserId(userId: String): List<BarcodeResponseDto> {
        return barcodeRepository.findAllByUserId(userId)
            .map { barcode -> BarcodeResponseDto.from(barcode) }
    }

    private fun uploadBarcodeImage(barcodeImage: String): String {
        return imageService.uploadBarcodeToS3(barcodeImage)
    }

    private fun generateBarcodeImage(barcodeText: String): String {
        return barcodeGenerator.generateBarcodeImage(barcodeText)
    }

    private fun saveBarcode(userId: String, productId: Long, barcodeUrl: String): Barcode {
        val barcode = Barcode.create(userId, productId, barcodeUrl)
        return barcodeRepository.save(barcode)
    }

    companion object {
        private fun generateBarcodeText(userId: String, productId: Long): String {
            return String.format(BarcodeConstants.BARCODE_TEXT_TEMPLATE, productId, userId)
        }
    }
}
