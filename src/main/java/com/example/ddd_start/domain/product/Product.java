package com.example.ddd_start.domain.product;

import com.example.ddd_start.domain.common.Money;
import com.example.ddd_start.domain.order.OrderLine;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
  @OneToMany(mappedBy = "product")
  private List<OrderLine> orderLines;

  public Product(String name, Money price, List<OrderLine> orderLines) {
    this.name = name;
    this.price = new Money(price.getValue());
    this.orderLines = orderLines;
  }
}
