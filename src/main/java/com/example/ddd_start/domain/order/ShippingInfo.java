package com.example.ddd_start.domain.order;

import com.example.ddd_start.domain.common.Address;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Getter;

@Getter
@Embeddable
public class ShippingInfo {

  @Embedded
  private Address address;
  @Embedded
  private Receiver receiver;
}
