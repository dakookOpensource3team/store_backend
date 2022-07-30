package com.example.ddd_start.domain.customer;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Customer {

  @Id
  private Long id;
  private String name;

  public Customer(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Customer(String name) {
    this.name = name;
  }
}
