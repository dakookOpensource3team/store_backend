package com.example.ddd_start.domain.order.dto;

import com.example.ddd_start.domain.member.Member;
import com.example.ddd_start.domain.order.Orders;
import com.example.ddd_start.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

  private Orders orders;
  private Member member;
  private Product product;
}
