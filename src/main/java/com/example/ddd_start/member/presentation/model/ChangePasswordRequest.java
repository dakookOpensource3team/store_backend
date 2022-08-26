package com.example.ddd_start.member.presentation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangePasswordRequest {

  private final Long memberId;
  private final String curPw;
  private final String newPw;
}
