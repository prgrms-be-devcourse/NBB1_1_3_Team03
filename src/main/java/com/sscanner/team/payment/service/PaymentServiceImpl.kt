package com.sscanner.team.payment.service;

import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.service.BarcodeService;
import com.sscanner.team.payment.entity.PaymentRecord;
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto;
import com.sscanner.team.points.service.PointService;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.payment.repository.PaymentRepository;
import com.sscanner.team.payment.requestdto.PointPaymentRequestDto;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ProductService productService;
    private final PointService pointService;
    private final PaymentRepository paymentRepository;
    private final BarcodeService barcodeService;

    @Transactional
    @Override
    public PointPaymentResponseDto processPointPayment(PointPaymentRequestDto pointPaymentRequestDto) {
        String userId = pointPaymentRequestDto.userId();
        Long productId = pointPaymentRequestDto.productId();

        Product product = productService.findById(productId);
        Integer productPrice = product.getPrice();

        processPointDeduction(userId, productPrice);

        UserPoint userPoint = pointService.findByUserId(userId);

        Barcode barcode = barcodeService.createAndSaveBarcode(userId, productId);

        PaymentRecord paymentRecord = createPaymentRecord(userPoint, product, productPrice, barcode);
        paymentRepository.save(paymentRecord);

        Integer updatedPoint = pointService.fetchCachedPoint(userId);
        return PointPaymentResponseDto.of(userId, updatedPoint);
    }

    private PaymentRecord createPaymentRecord(UserPoint userPoint, Product product, Integer productPrice, Barcode barcode) {
        return PaymentRecord.builder()
                .id(UUID.randomUUID())
                .user(userPoint.getUser())
                .product(product)
                .payment(productPrice)
                .barcodeUrl(barcode.getBarcodeUrl())
                .build();
    }

    private void processPointDeduction(String userId, Integer productPrice) {
        Integer currentPoint = pointService.fetchCachedPoint(userId);
        validateSufficientPoints(currentPoint, productPrice);

        pointService.decrementPoint(userId, productPrice);
        pointService.markUserForBackup(userId);
    }

    private void validateSufficientPoints(Integer currentPoint, Integer productPrice) {
        if (currentPoint == null || currentPoint < productPrice) {
            throw new BadRequestException(ExceptionCode.NOT_ENOUGH_POINTS);
        }
    }
}
