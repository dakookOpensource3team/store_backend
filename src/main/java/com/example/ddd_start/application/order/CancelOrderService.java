package com.example.ddd_start.application.order;

import com.example.ddd_start.domain.order.Order;
import com.example.ddd_start.domain.order.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelOrderService {

  private final OrderRepository orderRepository;

  @Transactional
  public void cancelOrder(Long orderId) {
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    optionalOrder.ifPresent(Order::cancel);
  }
}
