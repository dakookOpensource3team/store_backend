package com.example.ddd_start.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  public Optional<Cart> findByMemberIdAndProductId(Long memberId, Long productId);
  public List<Cart> findByMemberId(Long memberId);
  public void deleteAllByMemberId(Long memberId);
}
