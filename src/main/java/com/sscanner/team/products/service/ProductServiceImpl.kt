package com.sscanner.team.products.service;

import com.sscanner.team.products.entity.Product;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.products.entity.ProductImg;
import com.sscanner.team.products.repository.ProductRepository;
import com.sscanner.team.products.responsedto.ProductImgResponseDto;
import com.sscanner.team.products.responsedto.ProductWithImgResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImgService productImgService;

    @Override
    public Map<String, Object> findAllWithImgs(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        List<Long> productIds = extractProductIds(products);

        Map<Long, List<ProductImg>> productImgsMap = productImgService.findImgsGroupedByProductId(productIds);

        List<ProductWithImgResponseDto> productWithImgDtos = new ArrayList<>();
        for (Product product : products) {
            List<ProductImg> productImgs = productImgsMap.getOrDefault(product.getId(), Collections.emptyList());
            productWithImgDtos.add(toProductWithImgDto(product, productImgs));
        }

        return createResponse(productWithImgDtos, products);
    }

    @Override
    public ProductWithImgResponseDto findWithImgById(Long productId) {
        Product product = findById(productId);
        List<ProductImg> productImgs = productImgService.findByProductId(productId);
        return toProductWithImgDto(product, productImgs);
    }

    @Transactional
    @Override
    public List<ProductImgResponseDto> addImages(Long productId, List<MultipartFile> files) {
        return productImgService.uploadImages(productId, files).stream()
                .map(ProductImgResponseDto::from)
                .toList();
    }

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PRODUCT_ID));
    }

    @Override
    public Map<Long, Product> findProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }
        return productMap;
    }

    private List<Long> extractProductIds(Page<Product> products) {
        return products.stream()
                .map(Product::getId)
                .toList();
    }

    private ProductWithImgResponseDto toProductWithImgDto(Product product, List<ProductImg> productImgs) {
        List<String> imgUrls = productImgs.stream()
                .map(ProductImg::getUrl)
                .toList();

        return ProductWithImgResponseDto.from(product, imgUrls);
    }

    private Map<String, Object> createResponse(List<ProductWithImgResponseDto> productWithImgDtos, Page<Product> products) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productWithImgDtos);
        response.put("currentPage", products.getNumber() + 1);
        response.put("totalItems", products.getTotalElements());
        response.put("totalPages", products.getTotalPages());

        return response;
    }
}
