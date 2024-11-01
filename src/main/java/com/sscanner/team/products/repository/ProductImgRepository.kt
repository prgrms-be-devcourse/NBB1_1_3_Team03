package com.sscanner.team.products.repository;

import com.sscanner.team.products.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {
    List<ProductImg> findAllByProductId(Long productId);
    List<ProductImg> findAllByProductIdIn(List<Long> productIds);
}
