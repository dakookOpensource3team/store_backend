package com.example.ddd_start.member.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  List<Member> findMemberByNameLike(String name, Pageable pageable);

  Page<Member> findPageMemberByNameLike(String name, Pageable pageable);

  List<Member> findByIdIn(Long[] blockingIds);
}
