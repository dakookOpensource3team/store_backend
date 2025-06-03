package com.example.ddd_start.order.application.model;

import com.example.ddd_start.common.domain.Money;

public record CartDto(Long id,
                      String productName,
                      Money price,
                      String imageUrl,
                      Integer quantity) {

}
