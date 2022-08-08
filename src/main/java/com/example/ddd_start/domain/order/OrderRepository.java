package com.example.ddd_start.domain.order;

import com.example.ddd_start.domain.order.dto.OrderResponseDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

  @Query(
      "select new com.example.ddd_start.domain.order.dto.OrderResponseDto(o, m, p) "
          + "from orders o join o.orderLines ol, Member m, Product p "
          + "where o.id =: orderId and "
          + "o.orderer.memberId =: memberId and "
          + "index(ol) = 0 and "
          + "ol.product_id = p.id")
  List<OrderResponseDto> findOrders(Long orderId, Long memberId, Long productId);
}
