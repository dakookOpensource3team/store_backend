package com.example.ddd_start.domain.order;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;

@Entity(name = "order")
@Getter
public class Order {

  @Id
  private Long id;
  private String orderNumber;
  private OrderState orderState;
  private ShippingInfo shippingInfo;

}
