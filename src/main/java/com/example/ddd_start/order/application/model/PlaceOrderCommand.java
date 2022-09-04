package com.example.ddd_start.order.application.model;

import com.example.ddd_start.coupon.domain.Coupon;
import com.example.ddd_start.order.domain.OrderLine;
import com.example.ddd_start.order.domain.value.Orderer;
import com.example.ddd_start.order.domain.value.ShippingInfo;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlaceOrderCommand {

  private final List<OrderLine> orderLines;
  private final ShippingInfo shippingInfo;
  private final Orderer orderer;
  private final List<Coupon> coupons;
}
