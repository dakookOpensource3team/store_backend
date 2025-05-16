package com.example.ddd_start.coupon.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  public void deleteAllByMemberId(Long memberId);

}
