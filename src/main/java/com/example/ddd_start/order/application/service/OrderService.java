package com.example.ddd_start.order.application.service;

import com.example.ddd_start.common.domain.exception.NoOrderException;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.order.application.model.ChangeOrderShippingInfoCommand;
import com.example.ddd_start.order.application.model.PlaceOrderCommand;
import com.example.ddd_start.order.domain.Order;
import com.example.ddd_start.order.domain.OrderRepository;
import com.example.ddd_start.order.domain.dto.OrderDto;
import com.example.ddd_start.order.domain.value.ShippingInfo;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void cancelOrder(Long orderId) {
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    Order order = optionalOrder.orElseThrow(NoOrderException::new);
    order.cancel();
  }

  @Transactional
  public void changeShippingInfo(ChangeOrderShippingInfoCommand changeOrderShippingInfoCommand) {
    Optional<Order> optionalOrder = orderRepository.findById(
        changeOrderShippingInfoCommand.getOrderId());
    Order order = optionalOrder.orElseThrow(NoOrderException::new);
    ShippingInfo newShippingInfo = changeOrderShippingInfoCommand.getShippingInfo();
    order.changeShippingInfo(newShippingInfo);

    if (changeOrderShippingInfoCommand.isUseNewShippingAddressAsMemberAddress()) {
      Optional<Member> optionalMember = memberRepository.findById(order.getOrderer().getMemberId());
      Member member = optionalMember.orElseThrow(NoSuchElementException::new);
      member.changeAddress(newShippingInfo.getAddress());
    }
  }

  @Transactional(readOnly = true)
  public void findOrders() {
    List<OrderDto> allOrder = orderRepository.search();
    allOrder.forEach(
        e -> log.info("id: " + e.getOrderNumber())
    );
  }

  @Transactional
  public Long placeOrder(PlaceOrderCommand placeOrderCommand) {
    Order order = new Order(placeOrderCommand.getOrderLines(), placeOrderCommand.getShippingInfo(),
        placeOrderCommand.getOrderer());
    orderRepository.save(order);
    return order.getId();
  }
}
