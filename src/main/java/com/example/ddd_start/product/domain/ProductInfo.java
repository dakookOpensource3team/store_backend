package com.example.ddd_start.product.domain;

import lombok.Getter;

@Getter
public class ProductInfo {

  private final String name;
  private final Integer price;
  private Long categoryId;

  public ProductInfo(String name, Integer price) {
    this.name = name;
    this.price = price;
  }

  public ProductInfo(String name, Integer price, Long categoryId) {
    this.name = name;
    this.price = price;
    this.categoryId = categoryId;
  }
}
