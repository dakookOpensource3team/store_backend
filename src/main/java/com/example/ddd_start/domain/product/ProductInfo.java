package com.example.ddd_start.domain.product;

import com.example.ddd_start.domain.common.Money;
import lombok.Getter;

@Getter
public class ProductInfo {

  private final String name;
  private final Money price;
  private Long categoryId;

  public ProductInfo(String name, Money price) {
    this.name = name;
    this.price = price;
  }

  public ProductInfo(String name, Money price, Long categoryId) {
    this.name = name;
    this.price = price;
    this.categoryId = categoryId;
  }
}
