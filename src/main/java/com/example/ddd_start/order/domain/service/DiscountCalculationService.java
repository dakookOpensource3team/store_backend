package com.example.ddd_start.order.domain.service;

import com.example.ddd_start.common.domain.Money;
import com.example.ddd_start.coupon.application.model.CouponDto;
import com.example.ddd_start.coupon.domain.Coupon;
import com.example.ddd_start.coupon.domain.CouponRepository;
import com.example.ddd_start.member.domain.MemberGrade;
import com.example.ddd_start.order.domain.OrderLine;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiscountCalculationService {
  private final CouponRepository couponRepository;

  @Transactional
  public Money calculateDiscountAmounts(
      List<OrderLine> orderLines,
      List<CouponDto> couponDtos,
      MemberGrade grade
  ) {
    List<Coupon> coupons = couponDtos.stream()
        .map(couponDto ->
            couponRepository.findById(couponDto.id())
                .orElseThrow(() -> new RuntimeException("Coupon not found")))
        .toList();

    Money discountedAmount = orderLines.stream()
        .map(orderLine -> calculateDiscount(orderLine, coupons))
        .reduce(new Money(0), (v1, v2) -> v1.add(v2));
    coupons
        .forEach(Coupon::useCoupon);

    couponRepository.saveAll(coupons);

    return discountedAmount;
  }

  private Money calculateDiscount(List<OrderLine> orderLines, Coupon coupon) {
    return null;
  }

  private Money calculateDiscount(OrderLine orderLine, List<Coupon> coupons) {
    Money amount = orderLine.getAmount();
    return coupons.stream()
        .map(coupon -> coupon.redeemCoupon(amount))
        .reduce(new Money(0), (v1, v2) -> v1.add(v2));
  }

  private Money calculateDiscount(MemberGrade grade) {
    return null;
  }

}
