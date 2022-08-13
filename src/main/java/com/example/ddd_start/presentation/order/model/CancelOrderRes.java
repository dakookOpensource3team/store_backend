package com.example.ddd_start.presentation.order.model;

import com.example.ddd_start.domain.common.Money;
import com.example.ddd_start.domain.order.value.OrderState;

public class CancelOrderRes {

  private Long orderId;
  private String orderNumber;
  private OrderState orderState;
  private Money total_amount;
}
