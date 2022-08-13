package com.example.ddd_start.application.service.product;

import com.example.ddd_start.domain.common.Money;
import lombok.Getter;

@Getter
public class NewProductRequest {

  private final Long storeId;
  private final String name;
  private final Money price;
  private Long categoryId;

  public NewProductRequest(Long storeId, String name, Money price) {
    this.storeId = storeId;
    this.name = name;
    this.price = price;
  }

  public NewProductRequest(Long storeId, String name, Money price, Long categoryId) {
    this.storeId = storeId;
    this.name = name;
    this.price = price;
    this.categoryId = categoryId;
  }
}
