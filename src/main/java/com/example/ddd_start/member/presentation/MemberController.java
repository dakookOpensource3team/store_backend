package com.example.ddd_start.member.presentation;

import com.example.ddd_start.auth.model.JwtToken;
import com.example.ddd_start.common.domain.exception.DuplicateEmailException;
import com.example.ddd_start.common.domain.exception.NoMemberFoundException;
import com.example.ddd_start.member.applicaiton.ChangePasswordService;
import com.example.ddd_start.member.applicaiton.JoinMemberService;
import com.example.ddd_start.member.applicaiton.MemberService;
import com.example.ddd_start.member.applicaiton.model.*;
import com.example.ddd_start.member.presentation.model.ChangePasswordRequest;
import com.example.ddd_start.member.presentation.model.JoinMemberRequest;
import com.example.ddd_start.member.presentation.model.MemberResponse;
import com.example.ddd_start.member.presentation.model.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final JoinMemberService joinMemberService;
  private final ChangePasswordService changePasswordService;
  private final MemberService memberService;

  @PostMapping("/members/join")
  public ResponseEntity join(@RequestBody JoinMemberRequest req, Errors errors){
    String email = req.getEmail();
    String password = req.getPassword();
    String username = req.getUsername();
    AddressCommand addressReq = req.getAddressReq();
    String role = req.getRole();

    try {
      joinResponse joinResponse = joinMemberService.joinMember(
          new joinCommand(email, password, username, addressReq, role));

      return new ResponseEntity<MemberResponse>(
          new MemberResponse(
              joinResponse.getMemberId(), joinResponse.getName(), "회원가입을 축하드립니다."),
          HttpStatus.ACCEPTED);
    } catch (DuplicateEmailException e) {
      errors.rejectValue(e.getMessage(), "duplicate");
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/members/change-password")
  public ResponseEntity changePassword(ChangePasswordRequest req) throws NoMemberFoundException {
    changePasswordService.changePassword(
        new ChangePasswordCommand(
            req.getMemberId(),
            req.getCurPw(),
            req.getNewPw()));

    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @PostMapping("/members/sign-in")
  public ResponseEntity signIn(@RequestBody SignInRequest req) {
    JwtToken jwtToken = memberService.signIn(new SignInCommand(
            req.username(),
            req.password()
    ));
    return new ResponseEntity(jwtToken, HttpStatus.ACCEPTED);
  }
}
