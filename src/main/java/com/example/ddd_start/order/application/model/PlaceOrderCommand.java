package com.example.ddd_start.order.application.model;

import com.example.ddd_start.coupon.application.model.CouponDto;
import com.example.ddd_start.order.domain.dto.OrderLineDto;
import com.example.ddd_start.order.domain.value.Orderer;
import com.example.ddd_start.order.domain.value.ShippingInfo;
import java.util.List;

public record PlaceOrderCommand(List<OrderLineDto> orderLines, ShippingInfo shippingInfo,
                                Orderer orderer, List<CouponDto> coupons) {

}
