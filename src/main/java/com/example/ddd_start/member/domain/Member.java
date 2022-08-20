package com.example.ddd_start.member.domain;

import com.example.ddd_start.common.domain.Address;
import com.example.ddd_start.common.domain.exception.PasswordNotMatchException;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Embedded
  private Password password;
  @Embedded
  private Address address;

  public void changePassword(String currentPassword, String changePassword) {
    if (!password.match(currentPassword)) {
      throw new PasswordNotMatchException();
    }
    this.password = new Password(changePassword);
  }

  public void changeAddress(Address address) {
    this.address = address;
  }
}
