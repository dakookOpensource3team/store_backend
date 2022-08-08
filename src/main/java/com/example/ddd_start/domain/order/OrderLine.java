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
  private Long product_id;
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Orders orders;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "amount"))
  private Money amount;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "price"))
  private Money price;
  private Integer quantity;

  public OrderLine(Product product, Orders orders, Integer quantity) {
    changeProduct(product);
    changeOrder(orders);
    this.price = product.getPrice();
    this.amount = calculateAmount();
    this.quantity = quantity;
  }

  private Money calculateAmount() {
    return this.amount.multiply(quantity);
  }

  private void changeOrder(Orders orders) {
    this.orders = orders;
    orders.getOrderLines().add(this);
  }

  private void changeProduct(Product product) {
    this.product_id = product.getId();
    product.getOrderLines().add(this);
  }
}
