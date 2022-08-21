package com.example.ddd_start.order.domain;

import com.example.ddd_start.order.domain.dto.OrderResponseDto;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, OrderRepositoryCustom {

  @Query(value =
      "select new com.example.ddd_start.order.domain.dto.OrderResponseDto(o, m, p) "
          + "from orders o join o.orderLines ol, Member m, Product p "
          + "where o.orderer.memberId = :memberId "
          + "and o.orderer.memberId = m.id "
          + "and ol.product_id = p.id")
  List<OrderResponseDto> findOrdersByMemberId(@Param("memberId") Long memberId);

  List<Order> findOrdersByIdOrderByCreatedAtDesc(Long id);

  List<Order> findOrdersByIdOrderByCreatedAtDescTotalAmounts(Long id);

  List<Order> findAllByIdAndCreatedAtBetween(Long id, Instant startAt, Instant endedAt);
}
