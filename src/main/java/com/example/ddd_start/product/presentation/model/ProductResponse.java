package com.example.ddd_start.product.presentation.model;

import com.example.ddd_start.product.application.service.model.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private List<ProductDTO> productDTOS;

    public ProductResponse(List<ProductDTO> productDTOS) {
        this.productDTOS = productDTOS;
    }
}
