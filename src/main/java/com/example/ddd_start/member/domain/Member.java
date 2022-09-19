package com.example.ddd_start.member.domain;

import com.example.ddd_start.common.domain.Address;
import com.example.ddd_start.common.domain.exception.PasswordNotMatchException;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(columnDefinition = "varchar(30)")
  private String email;
  @Column(columnDefinition = "varchar(15)")
  private String name;
  @Embedded
  private Password password;
  @Embedded
  private Address address;
  private Boolean blocked;
  @Enumerated(EnumType.STRING)
  private MemberGrade memberGrade;

  public Member(String name, String email, Password password, Address address) {
    this.name = name;
    this.password = password;
    this.address = address;
    this.blocked = false;
  }

  public void changePassword(String currentPassword, String changePassword) {
    if (!password.match(currentPassword)) {
      throw new PasswordNotMatchException();
    }
    this.password = new Password(changePassword);
  }

  public void changeAddress(Address address) {
    this.address = address;
  }

  public void block() {
    this.blocked = true;
  }
}
