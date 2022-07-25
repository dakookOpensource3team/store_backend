package com.example.ddd_start.domain.order;

public enum OrderState {
  PAYMENT_WAITING(true),
  PREPARING(true),
  SHIPPED(false),
  DELIVERING(false),
  DELIVERY_COMPLETED(false);

  private Boolean isShippingChangeable;

  OrderState(Boolean isShippingChangeable) {
    this.isShippingChangeable = isShippingChangeable;
  }

  public Boolean getIsShippingChangeable() {
    return isShippingChangeable;
  }
}
