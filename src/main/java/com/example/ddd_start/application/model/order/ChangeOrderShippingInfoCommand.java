package com.example.ddd_start.application.model.order;

import com.example.ddd_start.domain.order.value.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeOrderShippingInfoCommand {

  private Long orderId;
  private ShippingInfo shippingInfo;
  private boolean useNewShippingAddressAsMemberAddress;

}
