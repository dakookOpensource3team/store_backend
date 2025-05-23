package com.example.ddd_start.order.application.service;

import com.example.ddd_start.common.domain.error.ValidationError;
import com.example.ddd_start.common.domain.exception.NoOrderException;
import com.example.ddd_start.common.domain.exception.ValidationErrorException;
import com.example.ddd_start.common.domain.exception.VersionConflictException;
import com.example.ddd_start.coupon.application.model.CouponDto;
import com.example.ddd_start.coupon.domain.Coupon;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.order.application.model.ChangeOrderShippingInfoCommand;
import com.example.ddd_start.order.application.model.PlaceOrderCommand;
import com.example.ddd_start.order.application.model.StartShippingCommand;
import com.example.ddd_start.order.domain.Order;
import com.example.ddd_start.order.domain.OrderLine;
import com.example.ddd_start.order.domain.OrderRepository;
import com.example.ddd_start.order.domain.dto.OrderDto;
import com.example.ddd_start.order.domain.dto.OrderLineDto;
import com.example.ddd_start.order.domain.service.DiscountCalculationService;
import com.example.ddd_start.order.domain.value.ShippingInfo;
import com.example.ddd_start.product.domain.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final DiscountCalculationService discountCalculationService;
  private final ApplicationEventPublisher eventPublisher;
  private final ProductRepository productRepository;

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

    order.getOrderEvents().forEach(e -> eventPublisher.publishEvent(e));
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
    List<OrderLineDto> orderLineDtos = placeOrderCommand.orderLines();
    List<OrderLine> orderLines = orderLineDtos.stream()
        .map(orderLineDto ->
            new OrderLine(
                productRepository.findById(orderLineDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found")),
                orderLineDto.price(),
                orderLineDto.quantity()
            )).toList();

    Order order = new Order(orderLines, placeOrderCommand.shippingInfo(),
        placeOrderCommand.orderer());
    order = calculatePaymentInfo(order, placeOrderCommand.coupons());
    orderRepository.save(order);
    return order.getId();
  }

  @Transactional
  public Long placeOrderV2(PlaceOrderCommand command) throws ValidationErrorException {
    List<ValidationError> errors = new ArrayList<>();

    if (command == null) {
      errors.add(ValidationError.of("empty"));
    } else {
      if (command.orderer() == null) {
        errors.add(ValidationError.of("orderer", "empty"));
      }
      if (command.orderLines() == null) {
        errors.add(ValidationError.of("orderLine", "empty"));
      }
      if (command.shippingInfo() == null) {
        errors.add(ValidationError.of("shippingInfo", "empty"));
      }
    }

    if (!errors.isEmpty()) {
      throw new ValidationErrorException(errors);
    }

    List<OrderLineDto> orderLineDtos = command.orderLines();
    List<OrderLine> orderLines = orderLineDtos.stream()
        .map(orderLineDto ->
            new OrderLine(
                productRepository.findById(orderLineDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found")),
                orderLineDto.price(),
                orderLineDto.quantity()
            )).toList();

    Order order = calculatePaymentInfo(
        new Order(
            orderLines,
            command.shippingInfo(),
            command.orderer()
        ),
        command.coupons()
    );
    orderRepository.save(order);
    return order.getId();
  }

  private Order calculatePaymentInfo(Order order, List<CouponDto> coupons) {
    Member member = memberRepository.findById(order.getOrderer().getMemberId())
        .orElseThrow(NoSuchElementException::new);

    order.calculateAmounts(discountCalculationService, member.getMemberGrade(), coupons);

    return order;
  }

  @Transactional
  public void startShipping(StartShippingCommand command) {
    Order order = orderRepository.findById(command.getId()).orElseThrow(NoOrderException::new);
    if (!order.matchVersion(command.getVersion())) {
      throw new VersionConflictException();
    }

    order.changeShipped();
  }
}
