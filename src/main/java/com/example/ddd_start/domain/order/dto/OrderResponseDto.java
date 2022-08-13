package com.example.ddd_start.domain.order.dto;

import com.example.ddd_start.domain.member.Member;
import com.example.ddd_start.domain.order.Orders;
import com.example.ddd_start.domain.product.Product;
import lombok.AllArgsConstructor;
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
