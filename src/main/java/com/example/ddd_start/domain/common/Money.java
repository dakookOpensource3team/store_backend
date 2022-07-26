package com.example.ddd_start.domain.common;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Money {

  private Integer value;

  public Money(Integer value) {
    this.value = value;
  }

  public Money add(Money money) {
    return new Money(this.value + money.value);
  }

  public Money multiply(int quantity) {
    return new Money(this.value * quantity);
  }

  private Money subtract(Money money) {
    return new Money(this.value - money.value);
  }
}
