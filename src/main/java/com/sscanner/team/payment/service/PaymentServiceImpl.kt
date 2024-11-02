package com.sscanner.team.payment.service

import com.sscanner.team.barcode.entity.Barcode
import com.sscanner.team.barcode.service.BarcodeService
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.payment.entity.PaymentRecord
import com.sscanner.team.payment.repository.PaymentRepository
import com.sscanner.team.payment.requestdto.PointPaymentRequestDto
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto
import com.sscanner.team.points.entity.UserPoint
import com.sscanner.team.points.service.PointService
import com.sscanner.team.products.entity.Product
import com.sscanner.team.products.service.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PaymentServiceImpl(
    private val productService: ProductService,
    private val pointService: PointService,
    private val paymentRepository: PaymentRepository,
    private val barcodeService: BarcodeService
) : PaymentService {

    @Transactional
    override fun processPointPayment(pointPaymentRequestDto: PointPaymentRequestDto): PointPaymentResponseDto {
        val userId = pointPaymentRequestDto.userId
        val productId = pointPaymentRequestDto.productId

        val product = productService.findById(productId)
        val productPrice = product.price

        processPointDeduction(userId, productPrice)

        val userPoint = pointService.findByUserId(userId)

        val barcode = barcodeService.createAndSaveBarcode(userId, productId)

        val paymentRecord = createPaymentRecord(userPoint, product, productPrice, barcode)
        paymentRepository.save(paymentRecord)

        val updatedPoint = pointService.fetchCachedPoint(userId)
        return PointPaymentResponseDto.of(userId, updatedPoint)
    }

    private fun createPaymentRecord(
        userPoint: UserPoint,
        product: Product,
        productPrice: Int,
        barcode: Barcode
    ): PaymentRecord {
        return PaymentRecord.create(
            id = UUID.randomUUID(),
            user = userPoint.user,
            product = product,
            payment = productPrice,
            barcodeUrl = barcode.barcodeUrl
        )
    }

    private fun processPointDeduction(userId: String, productPrice: Int) {
        val currentPoint = pointService.fetchCachedPoint(userId)
        validateSufficientPoints(currentPoint, productPrice)

        pointService.decrementPoint(userId, productPrice)
        pointService.markUserForBackup(userId)
    }

    private fun validateSufficientPoints(currentPoint: Int, productPrice: Int) {
        if (currentPoint < productPrice) {
            throw BadRequestException(ExceptionCode.NOT_ENOUGH_POINTS)
        }
    }
}
