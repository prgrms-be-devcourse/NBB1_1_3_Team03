package com.sscanner.team.barcode.service

import com.sscanner.team.barcode.entity.Barcode
import com.sscanner.team.barcode.responsedto.BarcodeResponseDto

interface BarcodeService {
    fun createAndSaveBarcode(userId: String, productId: Long): Barcode
    fun findBarcodesByUserId(userId: String): List<BarcodeResponseDto>
}