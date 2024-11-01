package com.sscanner.team.products.repository

import com.sscanner.team.products.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>
