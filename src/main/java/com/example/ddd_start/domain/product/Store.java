package com.example.ddd_start.domain.product;

import static com.example.ddd_start.domain.product.StoreStatus.CLOSED;

import com.example.ddd_start.domain.common.exception.StoreBlockedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Store {

  @Id
  @GeneratedValue
  private Long id;
  private String storeName;
  @OneToMany(mappedBy = "store")
  private Set<Product> products = new HashSet<>();

  @Enumerated(value = EnumType.STRING)
  private StoreStatus storeStatus;

  public Store(String storeName, StoreStatus storeStatus) {
    this.storeName = storeName;
    this.storeStatus = storeStatus;
  }

  public Product createProduct(ProductInfo productInfo) {
    if (isStoreBlocked()) {
      throw new StoreBlockedException();
    }
    Product product = new Product(productInfo.getName(), productInfo.getPrice(),
        productInfo.getCategoryId(), this);
    products.add(product);
    return product;
  }

  private boolean isStoreBlocked() {
    return this.storeStatus == CLOSED;
  }
}
