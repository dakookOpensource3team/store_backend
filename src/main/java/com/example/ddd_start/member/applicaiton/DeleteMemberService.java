package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.product.domain.LastlyRetrieveProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMemberService {

  private final MemberRepository memberRepository;
  private final LastlyRetrieveProductRepository lastlyRetrieveProductRepository;

  @Transactional
  public void delete(Long id) {
    memberRepository.findById(id).ifPresent(member -> {
      memberRepository.delete(member);
      lastlyRetrieveProductRepository.deleteAllByMember(member);
    });
  }
}
