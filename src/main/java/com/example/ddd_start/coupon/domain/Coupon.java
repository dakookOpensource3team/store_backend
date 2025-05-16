package com.example.ddd_start.coupon.domain;

import com.example.ddd_start.common.domain.Money;
import com.example.ddd_start.coupon.Exception.CouponAlreadyUsedException;
import com.example.ddd_start.member.domain.Member;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Boolean isUsed;
  private Boolean isRatio;
  private Float ratio;
  @ManyToOne
  Member member;
  @Embedded
  private Money fixedAmount;

  public Coupon(String name, Boolean isUsed, Boolean isRatio, Float ratio, Member member,
      Money fixedAmount) {
    this.name = name;
    this.isUsed = isUsed;
    this.isRatio = isRatio;
    this.ratio = ratio;
    this.member = member;
    this.fixedAmount = fixedAmount;
  }

  public Money redeemCoupon(Money money) {
    if (this.isUsed) {
      throw new CouponAlreadyUsedException("Coupon is used");
    }

    if (isRatio) {
      return money.multiply(ratio);
    }
    return money.subtract(fixedAmount);
  }

  public void useCoupon(){
    isUsed = true;
  }
}
