package com.example.ddd_start.coupon.domain;

import com.example.ddd_start.common.domain.Money;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Boolean isUsed;
  private Boolean isRatio;
  private Float ratio;
  @Embedded
  private Money fixedAmount;
}
