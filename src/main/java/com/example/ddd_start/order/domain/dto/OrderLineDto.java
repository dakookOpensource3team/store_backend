package com.example.ddd_start.order.domain.dto;

public record OrderLineDto(Long productId, Long amount, Integer price, Integer quantity) {
}
