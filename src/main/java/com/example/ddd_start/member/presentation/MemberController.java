package com.example.ddd_start.member.presentation;

import com.example.ddd_start.member.applicaiton.MemberService;
import com.example.ddd_start.member.applicaiton.model.AddressRequest;
import com.example.ddd_start.member.applicaiton.model.joinRequest;
import com.example.ddd_start.member.applicaiton.model.joinResponse;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.member.presentation.model.JoinMemberRequest;
import com.example.ddd_start.member.presentation.model.MemberResponse;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/members/join")
  public ResponseEntity join(@RequestBody JoinMemberRequest req) {
    String email = req.getEmail();
    String password = req.getPassword();
    String name = req.getName();
    AddressRequest addressReq = req.getAddressReq();

    joinResponse joinResponse = memberService.joinMember(
        new joinRequest(email, password, name, addressReq));

    return new ResponseEntity<MemberResponse>(
        new MemberResponse(
            joinResponse.getMemberId(), joinResponse.getName(), "회원가입을 축하드립니다."),
        HttpStatus.ACCEPTED);
  }

  @GetMapping("/members/password")
  public ResponseEntity getPassword(Long id) {
    memberService.findPassword(id);

    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
}
