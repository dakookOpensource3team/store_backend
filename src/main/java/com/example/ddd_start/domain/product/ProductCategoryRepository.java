package com.example.ddd_start.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends
    JpaRepository<ProductCategory, Long> {

}
