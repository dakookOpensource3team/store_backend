package com.example.ddd_start.application.service.order;

import com.example.ddd_start.domain.calculate_rule_engine.CalculateRuleEngine;
import com.example.ddd_start.domain.common.exception.NoOrderException;
import com.example.ddd_start.domain.order.Order;
import com.example.ddd_start.domain.order.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final CalculateRuleEngine calculateRuleEngine;
  private final OrderRepository orderRepository;

  public void cancelOrder(Long orderId) {
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    Order order = optionalOrder.orElseThrow(NoOrderException::new);
    order.cancel();
  }
}
