package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public void findMemberByName(String name) {
    Sort sort = Sort.by("id").descending();

    PageRequest pageRequest = PageRequest.of(0, 10, sort);

    List<Member> result = memberRepository.findMemberByNameLike(name, pageRequest);
    result.forEach(
        e -> log.info("member Address: " + e.getAddress())
    );
  }

  public void findPageMemberByName(String name) {
    PageRequest pageRequest = PageRequest.of(0, 10);

    Page<Member> result = memberRepository.findPageMemberByNameLike(name,
        pageRequest);
  }
}
