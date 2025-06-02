package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.coupon.domain.UserCouponRepository;
import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.product.domain.LastlyRetrieveProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMemberService {

  private final MemberRepository memberRepository;
  private final LastlyRetrieveProductRepository lastlyRetrieveProductRepository;
  private final UserCouponRepository userCouponRepository;

  @Transactional
  public void delete(Long id) {
    memberRepository.findById(id).ifPresent(member -> {
      memberRepository.delete(member);
      lastlyRetrieveProductRepository.deleteAllByMember(member);
      userCouponRepository.deleteAllByMemberId(member.getId());
    });
  }
}
