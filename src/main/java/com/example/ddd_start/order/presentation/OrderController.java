package com.example.ddd_start.order.presentation;

import com.example.ddd_start.common.domain.exception.ValidationErrorException;
import com.example.ddd_start.coupon.Exception.CouponAlreadyUsedException;
import com.example.ddd_start.order.application.model.ChangeOrderShippingInfoCommand;
import com.example.ddd_start.order.application.model.PlaceOrderCommand;
import com.example.ddd_start.order.application.service.OrderService;
import com.example.ddd_start.order.presentation.model.PlaceOrderRequest;
import com.example.ddd_start.order.presentation.model.PlaceOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/orders")
  public void findAllOrders() {
    orderService.findOrders();
  }

  @PostMapping("/orders/place-order")
  public ResponseEntity order(@RequestBody PlaceOrderRequest req, BindingResult bindingResult) {
    try {
      Long orderId = orderService.placeOrderV2(
          new PlaceOrderCommand(
              req.orderLines(),
              req.shippingInfo(),
              req.orderer(),
              req.paymentInfo(),
              req.coupons()
          )
      );
      return ResponseEntity
          .ok(new PlaceOrderResponse("주문이 정상적으로 완료되었습니다.", orderId));
    } catch (ValidationErrorException e) {
      e.getErrors().forEach(err -> {
        if (err.hasName()) {
          bindingResult.rejectValue(err.getPropertyName(), err.getValue());
        } else {
          bindingResult.reject(err.getValue());
        }
      });

      throw new RuntimeException(e);
    } catch (CouponAlreadyUsedException e) {
      return ResponseEntity.badRequest().body("이미 사용한 쿠폰입니다.");
    }
  }

  @PostMapping("/orders/shipping-info")
  public void changeShippingInfo(ChangeOrderShippingInfoCommand command) {
    orderService.changeShippingInfo(command);
  }
}
