package com.example.ddd_start.application.service.product;

import com.example.ddd_start.domain.product.Product;
import com.example.ddd_start.domain.product.ProductRepository;
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
