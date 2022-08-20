package com.example.ddd_start.common.domain;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
  private String city;
  private String guGun;
  private String dong;
  private String bunji;
}
