package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.common.domain.exception.NoMemberFoundException;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;

public class ChangePasswordService {

  private MemberRepository memberRepository;

  public void changePassword(Long id, String curPw, String newPw) throws NoMemberFoundException {
    Member member = memberRepository.findById(id).orElseThrow(NoMemberFoundException::new);
    member.changePassword(curPw, newPw);
  }
}
