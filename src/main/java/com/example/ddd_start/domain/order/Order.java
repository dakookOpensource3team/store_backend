package com.example.ddd_start.domain.order;

import static com.example.ddd_start.domain.order.value.OrderState.*;
import static com.example.ddd_start.domain.order.value.OrderState.PAYMENT_WAITING;
import static com.example.ddd_start.domain.order.value.OrderState.PREPARING;
import static com.example.ddd_start.domain.order.value.OrderState.SHIPPED;

import com.example.ddd_start.domain.common.Money;
import com.example.ddd_start.domain.order.value.OrderState;
import com.example.ddd_start.domain.order.value.Orderer;
import com.example.ddd_start.domain.order.value.ShippingInfo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity(name = "orders")
@Getter
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String orderNumber;
  @Enumerated(value = EnumType.STRING)
  private OrderState orderState;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "receiver.name",
          column = @Column(name = "receiver_name")),
      @AttributeOverride(name = "receiver.phoneNumber",
          column = @Column(name = "receiver_phone_number"))
  })
  private ShippingInfo shippingInfo;
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  List<OrderLine> orderLines;
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "total_amounts"))
  private Money totalAmounts;
  @Embedded
  private Orderer orderer;


  public void Order(List<OrderLine> orderLines, ShippingInfo shippingInfo, Orderer orderer) {
    this.orderNumber = generateOrderNumber();
    this.orderState = PAYMENT_WAITING;
    setOrderLines(orderLines);
    setShippingInfo(shippingInfo);
    setOrderer(orderer);
  }

  private void setOrderer(Orderer orderer) {
    if (orderer == null) {
      throw new IllegalArgumentException("no orderer");
    }
    this.orderer = orderer;
  }

  private String generateOrderNumber() {
    LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    String orderNumber =
        "" + ldt.getYear() + ldt.getMonthValue() + ldt.getHour() + ldt.getMinute() + String.format(
            "%04d", ldt.toString().hashCode());
    return orderNumber;
  }

  private void setShippingInfo(ShippingInfo shippingInfo) {
    if (shippingInfo == null) {
      throw new IllegalArgumentException("no ShippingInfo");
    }
    this.shippingInfo = shippingInfo;
  }

  private void setOrderLines(List<OrderLine> orderLines) {
    if (orderLines.isEmpty() && orderLines == null) {
      throw new IllegalArgumentException("no OrderLine");
    }
    this.orderLines = orderLines;
    this.totalAmounts = calculateTotalAmounts();
  }

  private Money calculateTotalAmounts() {
    return new Money(this.orderLines.stream()
            .mapToInt(o -> o.getAmount().getValue())
            .sum());
  }

  public void changeShippingInfo(ShippingInfo shippingInfo) {
    verifyNotYetShipped();
    this.shippingInfo = shippingInfo;
  }


  public void completePayment() {
    if (orderState != PAYMENT_WAITING) {
      throw new IllegalStateException("이미 결제과 완료된 주문입니다.");
    }
    this.orderState = PREPARING;
  }

  public void changeShipped() {
    verifyNotYetShipped();
    if (this.orderState != PREPARING) {
      throw new IllegalStateException("결제과 완료됬을 때만 출고가 가능합니다.");
    }
    this.orderState = SHIPPED;
  }

  public void changeDelivering() {
    if (orderState != SHIPPED) {
      throw new IllegalStateException("출고가 안될 시 배달을 못합니다.");
    }
  }

  public void cancel() {
    verifyNotYetShipped();
    this.orderState = CANCEL;
  }

  private void verifyNotYetShipped() {
    if (orderState != PAYMENT_WAITING || orderState != PREPARING) {
      throw new IllegalStateException("이미 출고 됬습니다.");
    }
  }

}
