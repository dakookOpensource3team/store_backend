package com.example.ddd_start.member.applicaiton.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class joinRequest {

  String email;
  String password;
  String name;
  AddressRequest addressReq;
}
