package com.example.ddd_start.product.application.service;

import com.example.ddd_start.product.domain.Product;
import com.example.ddd_start.product.domain.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductListService {
  private final ProductRepository productRepository;

  public List<Product> getProductOfCategory(Long categoryId) {
    return productRepository.findByCategoryId(categoryId);
  }
}
