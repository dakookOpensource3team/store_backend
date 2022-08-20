package com.example.ddd_start.order.domain;

import com.example.ddd_start.order.domain.dto.OrderDto;
import java.util.List;

public interface OrderCustomRepository {

  List<OrderDto> findOrderByOrdererId(OrderSearchCondition orderSearchCondition);

}
