package com.example.ddd_start.application.service.product;

import com.example.ddd_start.domain.product.Product;
import com.example.ddd_start.domain.product.ProductRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductService {

  private final ProductRepository productRepository;

  public void removeOptions(Long productId, int optIdx) {
    Product product = productRepository.findById(productId)
        .orElseThrow(NoSuchElementException::new);

    product.removeOption(optIdx);
  }
}
