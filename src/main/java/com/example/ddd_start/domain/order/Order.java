package com.example.ddd_start.domain.order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.Getter;

@Entity(name = "orders")
@Getter
public class Order {

  @Id
  private Long id;
  private String orderNumber;
  @Enumerated(value = EnumType.STRING)
  private OrderState orderState;
  @Embedded
  private ShippingInfo shippingInfo;

}
