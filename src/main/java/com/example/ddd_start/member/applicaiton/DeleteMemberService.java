package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public void delete(Long id) {
    memberRepository.deleteById(id);
  }
}
