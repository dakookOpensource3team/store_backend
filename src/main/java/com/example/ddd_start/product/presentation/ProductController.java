package com.example.ddd_start.product.presentation;

import com.example.ddd_start.product.application.service.DeleteProductService;
import com.example.ddd_start.product.application.service.FetchProductService;
import com.example.ddd_start.product.application.service.ProductListService;
import com.example.ddd_start.product.application.service.RegisterProductService;
import com.example.ddd_start.product.application.service.model.ProductDTO;
import com.example.ddd_start.product.presentation.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductListService productListService;
    private final DeleteProductService deleteProductService;
    private final RegisterProductService registerProductService;
    private final FetchProductService fetchProductService;

    @GetMapping("/products")
    public ResponseEntity printProducts() {
        List<ProductDTO> productDTOS = productListService.printProducts();
        return new ResponseEntity(
                new ProductResponse(productDTOS),
                HttpStatus.ACCEPTED
        );
    }
}

