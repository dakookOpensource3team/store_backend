package com.example.ddd_start.domain.order;

import com.example.ddd_start.domain.common.Money;
import com.example.ddd_start.domain.product.Product;
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
public class OrderLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "amount"))
  private Money amount;
  private Integer quantity;

  public OrderLine(Product product, Order order, Money amount, Integer quantity) {
    changeProduct(product);
    changeOrder(order);
    this.amount = calculateAmount();
    this.quantity = quantity;
  }

  private Money calculateAmount() {
    return this.amount.multiply(quantity);
  }

  private void changeOrder(Order order) {
    this.order = order;
    order.getOrderLines().add(this);
  }

  private void changeProduct(Product product) {
    this.product = product;
    product.getOrderLines().add(this);
  }
}
