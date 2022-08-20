package com.example.ddd_start.order.infra;

import static com.example.ddd_start.order.domain.QOrder.order;

import com.example.ddd_start.order.domain.OrderCustomRepository;
import com.example.ddd_start.order.domain.OrderSearchCondition;
import com.example.ddd_start.order.domain.dto.OrderDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

public class OrderCustomRepositoryImpl implements OrderCustomRepository {

  private final JPAQueryFactory queryFactory;

  public OrderCustomRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override

  public List<OrderDto> findOrderByOrdererId(OrderSearchCondition orderSearchCondition) {
    List<OrderDto> result = queryFactory.query()
        .select(Projections.constructor(OrderDto.class,
            order.orderNumber,
            order.orderState,
            order.shippingInfo,
            order.totalAmounts,
            order.orderer.name,
            order.createdAt
        ))
        .from(order)
        .where(ordererIdEq(orderSearchCondition.getOrdererId()))
        .fetch();

    return result;
  }

  private BooleanExpression ordererIdEq(Long ordererId) {
    return ordererId == null ? null : order.orderer.memberId.eq(ordererId);
  }
}
