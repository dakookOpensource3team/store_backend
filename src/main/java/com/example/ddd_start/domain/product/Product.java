package com.example.ddd_start.domain.product;

import com.example.ddd_start.domain.order.OrderLine;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Integer price;
  @OneToMany(mappedBy = "product")
  private List<OrderLine> orderLines;

}
