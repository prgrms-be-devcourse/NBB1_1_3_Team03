package com.sscanner.team.gifticon.service;

import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.repository.BarcodeRepository;
import com.sscanner.team.gifticon.responsedto.GifticonResponseDto;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.products.service.ProductImgService;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GifticonService {

    private final BarcodeRepository barcodeRepository;
    private final ProductService productService;
    private final ProductImgService productImgService;

    public List<GifticonResponseDto> getGifticonsByUserId(String userId) {
        List<Barcode> barcodes = barcodeRepository.findAllByUserId(userId);

        List<Long> productIds = extractProductIds(barcodes);

        Map<Long, Product> products = productService.findProductsByIds(productIds);
        Map<Long, String> productImages = productImgService.findMainImageUrlsByProductIds(productIds);

        return barcodes.stream()
                .map(barcode -> toGifticonResponseDto(barcode, products, productImages))
                .toList();
    }

    private List<Long> extractProductIds(List<Barcode> barcodes) {
        return barcodes.stream()
                .map(Barcode::getProductId)
                .toList();
    }

    private GifticonResponseDto toGifticonResponseDto(Barcode barcode, Map<Long, Product> products, Map<Long, String> productImages) {
        Product product = products.get(barcode.getProductId());
        String representativeProductImgUrl = productImages.get(barcode.getProductId());

        return GifticonResponseDto.of(product, representativeProductImgUrl, barcode.getBarcodeUrl());
    }
}

