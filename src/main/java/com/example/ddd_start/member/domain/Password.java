package com.example.ddd_start.member.domain;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Password {

  String password;

  public boolean match(String currentPassword) {
    return this.password.equals(currentPassword);
  }
}
