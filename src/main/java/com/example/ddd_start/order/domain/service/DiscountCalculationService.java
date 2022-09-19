package com.example.ddd_start.order.domain.service;

import com.example.ddd_start.common.domain.Money;
import com.example.ddd_start.coupon.domain.Coupon;
import com.example.ddd_start.member.domain.MemberGrade;
import com.example.ddd_start.order.domain.OrderLine;
import java.util.List;

public class DiscountCalculationService {

  public Money calculateDiscountAmounts(
      List<OrderLine> orderLines,
      List<Coupon> coupons,
      MemberGrade grade
  ) {
    Money couponDiscount =
        coupons.stream()
            .map(coupon -> calculateDiscount(coupon))
            .reduce(new Money(0), (v1, v2) -> v1.add(v2));

    Money membershipDiscount = calculateDiscount(grade);

    return couponDiscount.add(membershipDiscount);
  }

  private Money calculateDiscount(Coupon coupon) {
    //...
    return null;
  }

  private Money calculateDiscount(MemberGrade grade) {
    return null;
  }

}
