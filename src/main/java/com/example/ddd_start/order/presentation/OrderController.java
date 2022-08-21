package com.example.ddd_start.order.presentation;

import com.example.ddd_start.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/orders")
  public void findAllOrders() {
    orderService.findOrders();
  }

}
