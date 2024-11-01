package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.Product;
import com.sscanner.team.products.responsedto.ProductImgResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Map<String, Object> findAllWithImgs(Pageable pageable);
    ProductWithImgResponseDto findWithImgById(Long productId);
    List<ProductImgResponseDto> addImages(Long productId, List<MultipartFile> files);
    Product findById(Long id);
    Map<Long, Product> findProductsByIds(List<Long> productIds);
}
