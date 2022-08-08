package com.example.ddd_start.domain.product;

import com.example.ddd_start.domain.common.Money;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "price"))
  private Money price;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  public Product(String name, Money price) {
    this.name = name;
    this.price = new Money(price.getValue());
  }
}
