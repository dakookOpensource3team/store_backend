package com.example.ddd_start.domain.order;

import com.example.ddd_start.domain.common.Address;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ShippingInfo {

  private Address address;
  private Receiver receiver;
}
