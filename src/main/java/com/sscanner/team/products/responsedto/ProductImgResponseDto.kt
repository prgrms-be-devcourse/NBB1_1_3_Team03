package com.sscanner.team.products.responsedto;

import com.sscanner.team.products.entity.ProductImg;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductImgResponseDto (
        @NotNull
        Long productId,
        @NotEmpty
        String url
) {

    public static ProductImgResponseDto from(ProductImg productImg) {
        return new ProductImgResponseDto(
                productImg.getProductId(),
                productImg.getUrl()
        );
    }
}
