package com.example.ddd_start.domain.member;

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
