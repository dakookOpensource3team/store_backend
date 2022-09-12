package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.common.domain.exception.NoMemberFoundException;
import com.example.ddd_start.member.applicaiton.model.ChangePasswordCommand;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

public class ChangePasswordService {

  private MemberRepository memberRepository;

  @Transactional
  public void changePassword(ChangePasswordCommand req) throws NoMemberFoundException {
    Member member = memberRepository.findById(req.getMemberId())
        .orElseThrow(NoMemberFoundException::new);
    member.changePassword(req.getCurPw(), req.getNewPw());
  }
}
