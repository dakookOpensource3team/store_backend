package com.example.ddd_start.order.application.service;

import com.example.ddd_start.order.application.model.ChangeOrderShippingInfoCommand;
import com.example.ddd_start.common.domain.exception.NoOrderException;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.order.domain.OrderRepository;
import com.example.ddd_start.order.domain.Orders;
import com.example.ddd_start.order.domain.value.ShippingInfo;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public void cancelOrder(Long orderId) {
    Optional<Orders> optionalOrder = orderRepository.findById(orderId);
    Orders orders = optionalOrder.orElseThrow(NoOrderException::new);
    orders.cancel();
  }

  @Transactional
  public void changeShippingInfo(ChangeOrderShippingInfoCommand changeOrderShippingInfoCommand) {
    Optional<Orders> optionalOrder = orderRepository.findById(
        changeOrderShippingInfoCommand.getOrderId());
    Orders orders = optionalOrder.orElseThrow(NoOrderException::new);
    ShippingInfo newShippingInfo = changeOrderShippingInfoCommand.getShippingInfo();
    orders.changeShippingInfo(newShippingInfo);

    if (changeOrderShippingInfoCommand.isUseNewShippingAddressAsMemberAddress()) {
      Optional<Member> optionalMember = memberRepository.findById(orders.getOrderer().getMemberId());
      Member member = optionalMember.orElseThrow(NoSuchElementException::new);
      member.changeAddress(newShippingInfo.getAddress());
    }
  }
}
