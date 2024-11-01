package com.sscanner.team.products.repository;

import com.sscanner.team.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
