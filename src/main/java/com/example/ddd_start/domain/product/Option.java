package com.example.ddd_start.domain.product;

import com.example.ddd_start.domain.common.Money;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Option {

  private String name;
  @Embedded
  private Money price;
}
