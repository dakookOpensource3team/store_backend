package com.example.ddd_start.product.infrastructure;

import com.example.ddd_start.category.domain.CategoryRepository;
import com.example.ddd_start.common.domain.Money;
import com.example.ddd_start.product.application.service.model.ProductDTO;
import com.example.ddd_start.product.domain.Product;
import com.example.ddd_start.product.domain.ProductRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public static Product toEntity(ProductDTO productDTO) {
    return new Product(
        productDTO.getTitle(),
        productDTO.getSlug(),
        new Money(productDTO.getPrice()),
        productDTO.getDescription(),
        productDTO.getCategory().getId(),
        productDTO.getImages(),
        Instant.parse(productDTO.getCreationAt()),
        Instant.parse(productDTO.getUpdatedAt())
    );
  }
}
