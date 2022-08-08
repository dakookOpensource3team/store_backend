package com.example.ddd_start.domain.order.value;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Orderer {

  private Long memberId;
  private String name;
  private String phoneNumber;
  private String email;
}
