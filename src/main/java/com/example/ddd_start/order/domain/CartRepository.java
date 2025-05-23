package com.example.ddd_start.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  public List<Cart> findByMemberId(Long memberId);
  public void deleteAllByMemberId(Long memberId);
}
