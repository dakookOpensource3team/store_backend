package com.example.ddd_start.application.service.product;

import com.example.ddd_start.domain.product.Product;
import com.example.ddd_start.domain.product.ProductInfo;
import com.example.ddd_start.domain.product.ProductRepository;
import com.example.ddd_start.domain.product.Store;
import com.example.ddd_start.domain.product.StoreRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterProductService {

  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;

  @Transactional
  public Long registerNewProduct(NewProductRequest req) {
    Store store = storeRepository.findById(req.getStoreId())
        .orElseThrow(NoSuchElementException::new);
    Product product = store.createProduct(
        new ProductInfo(req.getName(), req.getPrice(), req.getCategoryId()));
    productRepository.save(product);

    return product.getId();
  }
}
