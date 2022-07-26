package com.example.ddd_start.domain.order.value;

import lombok.Getter;

public enum OrderState {
  PAYMENT_WAITING(true),
  PREPARING(true),
  SHIPPED(false),
  DELIVERING(false),
  DELIVERY_COMPLETED(false),
  CANCEL(false);

  private Boolean isShippingChangeable;

  OrderState(Boolean isShippingChangeable) {
    this.isShippingChangeable = isShippingChangeable;
  }

  public Boolean getIsShippingChangeable() {
    return isShippingChangeable;
  }
}
