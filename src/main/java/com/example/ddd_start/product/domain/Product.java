package com.example.ddd_start.product.domain;

import com.example.ddd_start.common.domain.Money;
import com.example.ddd_start.store.domain.Store;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
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
  private Long categoryId;
  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "prodcut_option",
      joinColumns = @JoinColumn(name = "product_id"))
  @OrderColumn(name = "list_idx")
  private List<Option> options = new ArrayList<>();

  public Product(String name, Money price, Long categoryId, Store store) {
    this.name = name;
    this.price = new Money(price.getAmount());
    this.categoryId = categoryId;
    this.store = store;
  }

  public void removeOption(int optionIdx) {
    this.options.remove(optionIdx);
  }
}
