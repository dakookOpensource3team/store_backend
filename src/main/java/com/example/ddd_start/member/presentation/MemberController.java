package com.example.ddd_start.member.presentation;

import com.example.ddd_start.common.domain.exception.DuplicateEmailException;
import com.example.ddd_start.common.domain.exception.NoMemberFoundException;
import com.example.ddd_start.member.applicaiton.ChangePasswordService;
import com.example.ddd_start.member.applicaiton.JoinMemberService;
import com.example.ddd_start.member.applicaiton.model.AddressCommand;
import com.example.ddd_start.member.applicaiton.model.ChangePasswordCommand;
import com.example.ddd_start.member.applicaiton.model.joinCommand;
import com.example.ddd_start.member.applicaiton.model.joinResponse;
import com.example.ddd_start.member.presentation.model.ChangePasswordRequest;
import com.example.ddd_start.member.presentation.model.JoinMemberRequest;
import com.example.ddd_start.member.presentation.model.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final JoinMemberService joinMemberService;
  private final ChangePasswordService changePasswordService;

  @PostMapping("/members/join")
  public ResponseEntity join(@RequestBody JoinMemberRequest req, Errors errors){
    String email = req.getEmail();
    String password = req.getPassword();
    String name = req.getName();
    AddressCommand addressReq = req.getAddressReq();

    try {
      joinResponse joinResponse = joinMemberService.joinMember(
          new joinCommand(email, password, name, addressReq));

      return new ResponseEntity<MemberResponse>(
          new MemberResponse(
              joinResponse.getMemberId(), joinResponse.getName(), "회원가입을 축하드립니다."),
          HttpStatus.ACCEPTED);
    } catch (DuplicateEmailException e) {
      errors.rejectValue(e.getMessage(), "duplicate");
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/members/change-password")
  public ResponseEntity changePassword(ChangePasswordRequest req) throws NoMemberFoundException {
    changePasswordService.changePassword(
        new ChangePasswordCommand(
            req.getMemberId(),
            req.getCurPw(),
            req.getNewPw()));

    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
}
