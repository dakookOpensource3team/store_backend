package com.example.ddd_start.product.domain;

import com.example.ddd_start.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LastlyRetrieveProductRepository extends JpaRepository<LastlyRetrieveProduct, Long> {
    public List<LastlyRetrieveProduct> findTop7LastlyRetrieveProductByMemberOrderByCreatedAtDesc(Member member);

}
