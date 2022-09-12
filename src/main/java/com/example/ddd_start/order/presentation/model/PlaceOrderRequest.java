package com.example.ddd_start.order.presentation.model;

import com.example.ddd_start.order.domain.OrderLine;
import com.example.ddd_start.order.domain.value.Orderer;
import com.example.ddd_start.order.domain.value.ShippingInfo;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlaceOrderRequest {

  private final List<OrderLine> orderLines;
  private final ShippingInfo shippingInfo;
  private final Orderer orderer;

}
