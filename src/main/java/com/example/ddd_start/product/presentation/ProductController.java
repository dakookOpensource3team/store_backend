package com.example.ddd_start.product.presentation;

import com.example.ddd_start.product.application.service.DeleteProductService;
import com.example.ddd_start.product.application.service.FetchProductService;
import com.example.ddd_start.product.application.service.PrintProductService;
import com.example.ddd_start.product.application.service.RegisterProductService;
import com.example.ddd_start.product.application.service.model.ProductDTO;
import com.example.ddd_start.product.presentation.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final PrintProductService printProductService;
    private final DeleteProductService deleteProductService;
    private final RegisterProductService registerProductService;
    private final FetchProductService fetchProductService;

    @GetMapping("/products")
    public ResponseEntity printAllProducts() {
        List<ProductDTO> products = printProductService.printAllProducts();
        return new ResponseEntity(
                products,
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity printProductById(@PathVariable Long productId) {
        ProductDTO product = printProductService.printProductById(productId);
        return new ResponseEntity(
            product,
            HttpStatus.ACCEPTED
        );
    }
}

