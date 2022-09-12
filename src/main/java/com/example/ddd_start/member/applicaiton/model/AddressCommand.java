package com.example.ddd_start.member.applicaiton.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressCommand {
  private String city;
  private String guGun;
  private String dong;
  private String bunji;
}
