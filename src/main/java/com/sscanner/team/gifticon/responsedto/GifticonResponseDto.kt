package com.sscanner.team.gifticon.responsedto;

import com.sscanner.team.products.entity.Product;

public record GifticonResponseDto(
        String productName,
        String productImageUrl,
        String barcodeImageUrl
) {
    public static GifticonResponseDto of(Product product, String productImageUrl, String barcodeImageUrl) {
        return new GifticonResponseDto(
                product.getName(),
                productImageUrl,
                barcodeImageUrl
        );
    }
}
