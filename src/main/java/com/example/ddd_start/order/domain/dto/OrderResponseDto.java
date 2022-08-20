package com.example.ddd_start.order.domain.dto;

import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.order.domain.Orders;
import com.example.ddd_start.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
  private Orders orders;
  private Member member;
  private Product product;

  public OrderResponseDto(Orders orders, Member member, Product product) {
    this.orders = orders;
    this.member = member;
    this.product = product;

  }
}
