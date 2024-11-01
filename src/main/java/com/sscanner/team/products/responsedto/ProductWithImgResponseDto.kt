package com.sscanner.team.products.responsedto;

import com.sscanner.team.products.entity.Product;

import java.util.List;

public record ProductWithImgResponseDto(
        Long productId,
        String productName,
        Integer price,
        List<String> imgUrls
) {
    public static ProductWithImgResponseDto from(Product product, List<String> imgUrls) {
        return new ProductWithImgResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                imgUrls
        );
    }
}
