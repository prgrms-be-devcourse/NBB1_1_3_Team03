package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.ProductImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductImgService {
    List<ProductImg> findByProductId(Long productId);
    Map<Long, List<ProductImg>> findImgsGroupedByProductId(List<Long> productIds);
    Map<Long, String> findMainImageUrlsByProductIds(List<Long> productIds);
    List<ProductImg> uploadImages(Long productId, List<MultipartFile> files);
}
