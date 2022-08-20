package com.example.ddd_start.common.domain;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Money {

  private Integer amount;

  public Money(Integer amount) {
    this.amount = amount;
  }

  public Money add(Money money) {
    return new Money(this.amount + money.amount);
  }

  public Money multiply(int quantity) {
    return new Money(this.amount * quantity);
  }

  private Money subtract(Money money) {
    return new Money(this.amount - money.amount);
  }

  private void setAmount(Integer amount) {
    this.amount = amount;
  }
}
