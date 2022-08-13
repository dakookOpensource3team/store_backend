package com.example.ddd_start.domain.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProductCategory {

  @Id
  @GeneratedValue
  Long id;
  private Long productId;
  private Long categoryId;
}
