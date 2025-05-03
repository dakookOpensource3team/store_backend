package com.example.ddd_start.product.application.service;

import com.example.ddd_start.category.application.service.model.CategoryDTO;
import com.example.ddd_start.category.domain.Category;
import com.example.ddd_start.category.domain.CategoryRepository;
import com.example.ddd_start.product.application.service.model.ProductDTO;
import com.example.ddd_start.product.domain.Product;
import com.example.ddd_start.product.domain.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductListService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDTO> printProducts() {
        List<Product> products = productRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<ProductDTO> productDTO = new ArrayList<>();

        for (Product product : products) {
            Category category = categories.stream()
                    .filter(it -> Objects.equals(it.getId(), product.getCategoryId()))
                    .findFirst()
                    .orElse(new Category());

            ProductDTO build = ProductDTO.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .slug(product.getSlug())
                    .price(product.getPrice().getAmount())
                    .description(product.getDescription())
                    .category(
                            CategoryDTO.builder()
                                    .id(category.getId())
                                    .slug(category.getSlug())
                                    .image(category.getImageUrl())
                                    .creationAt(category.getCreatedAt().toString())
                                    .updatedAt(category.getUpdatedAt().toString())
                                    .build()
                    )
                    .images(product.getImages())
                    .creationAt(product.getCreatedAt().toString())
                    .updatedAt(product.getUpdatedAt().toString())
                    .build();
            productDTO.add(build);
        }
        return productDTO;
    }
}
