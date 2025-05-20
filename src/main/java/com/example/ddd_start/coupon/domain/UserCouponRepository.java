package com.example.ddd_start.coupon.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

  public void deleteAllByMemberId(Long memberId);
  public List<UserCoupon> findAllByMemberIdAndIsUsedFalse(Long memberId);

}
