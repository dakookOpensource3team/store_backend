package com.example.ddd_start.product.presentation;

import com.example.ddd_start.product.application.service.DeleteProductService;
import com.example.ddd_start.product.application.service.FetchProductService;
import com.example.ddd_start.product.application.service.ProductListService;
import com.example.ddd_start.product.application.service.RegisterProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

  private final ProductListService productListService;
  private final DeleteProductService deleteProductService;
  private final RegisterProductService registerProductService;
  private final FetchProductService fetchProductService;


}
