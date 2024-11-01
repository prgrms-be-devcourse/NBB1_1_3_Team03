package com.sscanner.team.products.service;

import com.sscanner.team.global.common.service.ImageService;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.entity.ProductImg;
import com.sscanner.team.products.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductImgServiceImpl implements ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ImageService imageService;

    @Override
    public List<ProductImg> findByProductId(Long productId) {
        return productImgRepository.findAllByProductId(productId);
    }

    @Override
    public Map<Long, List<ProductImg>> findImgsGroupedByProductId(List<Long> productIds) {
        List<ProductImg> productImgs = productImgRepository.findAllByProductIdIn(productIds);

        Map<Long, List<ProductImg>> productImgsMap = new HashMap<>();

        for (ProductImg productImg : productImgs) {
            productImgsMap.computeIfAbsent(productImg.getProductId(), k -> new ArrayList<>()).add(productImg);
        }

        return productImgsMap;
    }

    @Override
    public Map<Long, String> findMainImageUrlsByProductIds(List<Long> productIds) {
        List<ProductImg> productImgs = productImgRepository.findAllByProductIdIn(productIds);
        Map<Long, String> mainImageMap = new HashMap<>();

        for (ProductImg productImg : productImgs) {
            mainImageMap.putIfAbsent(productImg.getProductId(), productImg.getUrl());
        }

        return mainImageMap;
    }

    @Transactional
    @Override
    public List<ProductImg> uploadImages(Long productId, List<MultipartFile> files) {
        List<ProductImg> productImgs = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String imgUrl = makeImgUrl(file);
                ProductImg saveProductImg = saveProductImg(productId, imgUrl);
                productImgs.add(saveProductImg);
            }
        } catch (Exception e) {
            throw new BadRequestException(ExceptionCode.FILE_UPLOAD_FAIL);
        }

        return productImgs;
    }

    private String makeImgUrl(MultipartFile file) {
        return imageService.makeImgUrl(file);
    }

    private ProductImg saveProductImg(Long productId, String imgUrl) {
        ProductImg productImg = ProductImg.builder()
                .productId(productId)
                .url(imgUrl)
                .build();

        return productImgRepository.save(productImg);
    }
}
