package com.sscanner.team.barcode.responsedto;

import com.sscanner.team.barcode.entity.Barcode;

public record BarcodeResponseDto(
        Long barcodeId,
        String barcodeUrl,
        Long productId
) {
    public static BarcodeResponseDto from(Barcode barcode) {
        return new BarcodeResponseDto(
                barcode.getId(),
                barcode.getBarcodeUrl(),
                barcode.getProductId()
        );
    }
}
