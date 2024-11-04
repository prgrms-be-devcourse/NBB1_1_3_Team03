package com.sscanner.team.barcode.service

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.sscanner.team.barcode.common.BarcodeConstants
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

@Component
class BarcodeGenerator {
    fun generateBarcodeImage(barcodeText: String): String {
        try {
            val bitMatrix = MultiFormatWriter().encode(
                barcodeText,
                BarcodeFormat.CODE_128,
                BarcodeConstants.BARCODE_WIDTH,
                BarcodeConstants.BARCODE_HEIGHT
            )
            ByteArrayOutputStream().use { baos ->
                MatrixToImageWriter.writeToStream(bitMatrix, BarcodeConstants.IMAGE_FORMAT, baos)
                return Base64.getEncoder().encodeToString(baos.toByteArray())
            }
        } catch (e: WriterException) {
            throw RuntimeException("Failed to generate barcode image due to writing error", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to generate barcode image due to I/O error", e)
        }
    }
}
