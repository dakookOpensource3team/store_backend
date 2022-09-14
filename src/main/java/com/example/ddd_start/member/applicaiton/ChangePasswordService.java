package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.common.domain.exception.NoMemberFoundException;
import com.example.ddd_start.member.applicaiton.model.ChangePasswordCommand;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordService {

  private final MemberRepository memberRepository;

  @Transactional
  public void changePassword(ChangePasswordCommand req) throws NoMemberFoundException {
    Member member = memberRepository.findById(req.getMemberId())
        .orElseThrow(NoMemberFoundException::new);
    member.changePassword(req.getCurPw(), req.getNewPw());
  }
}
