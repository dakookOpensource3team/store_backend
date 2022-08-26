package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.common.domain.exception.NoMemberFoundException;
import com.example.ddd_start.member.applicaiton.model.ChangePasswordCommand;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;

public class ChangePasswordService {

  private MemberRepository memberRepository;

  public void changePassword(ChangePasswordCommand req) throws NoMemberFoundException {
    Member member = memberRepository.findById(req.getMemberId())
        .orElseThrow(NoMemberFoundException::new);
    member.changePassword(req.getCurPw(), req.getNewPw());
  }
}
