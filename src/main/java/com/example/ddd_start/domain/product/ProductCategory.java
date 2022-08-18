package com.example.ddd_start.domain.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ProductCategory {

  @Id
  @GeneratedValue
  Long id;
  private Long productId;
  private Long categoryId;
}
