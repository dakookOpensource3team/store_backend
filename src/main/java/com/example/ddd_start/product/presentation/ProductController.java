package com.example.ddd_start.product.presentation;

import com.example.ddd_start.product.application.service.*;
import com.example.ddd_start.product.application.service.model.ProductDTO;
import com.example.ddd_start.product.presentation.model.SaveLastlyProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final PrintProductService printProductService;
    private final DeleteProductService deleteProductService;
    private final RegisterProductService registerProductService;
    private final FetchProductService fetchProductService;
    private final LastlyRetrieveProductService lastlyRetrieveProductService;

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

    @PostMapping("/products/lastly")
    public ResponseEntity saveLastlyRetrievedProduct(@RequestBody SaveLastlyProductRequest req) {
        lastlyRetrieveProductService.saveLastlyRetrieveProduct(req.memberId(), req.productId());
        return new ResponseEntity(
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/products/lastly")
    public ResponseEntity printLastlyRetrievedProduct(@RequestParam Long memberId) {
        List<ProductDTO> productDTOS = lastlyRetrieveProductService.printLastlyRetrieveProduct(memberId);
        return new ResponseEntity(
                productDTOS,
                HttpStatus.ACCEPTED
        );
    }
}

