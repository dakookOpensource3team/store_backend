package com.example.ddd_start.member.domain;

import com.example.ddd_start.common.domain.Address;
import com.example.ddd_start.common.domain.exception.PasswordNotMatchException;

import javax.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Member implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(columnDefinition = "varchar(30)")
  private String email;
  @Column(columnDefinition = "varchar(15)")
  private String username;
  private String password;
  @Embedded
  private Address address;
  private Boolean blocked;
  @Enumerated(EnumType.STRING)
  private MemberGrade memberGrade;

  @ElementCollection
  private List<String> roles = new ArrayList<>();

  @Override
  public Collection<?extends GrantedAuthority> getAuthorities(){
    return this.roles.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Member(String username, String email, String password, Address address, String role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.address = address;
    this.memberGrade = MemberGrade.GENERAL;
    this.blocked = false;
    this.roles.add(role);
  }

  public void changePassword(String currentPassword, String changePassword) {
    if (!password.equals(currentPassword)) {
      throw new PasswordNotMatchException();
    }
    this.password = changePassword;
  }

  public void changeAddress(Address address) {
    this.address = address;
  }

  public void block() {
    this.blocked = true;
  }
}
