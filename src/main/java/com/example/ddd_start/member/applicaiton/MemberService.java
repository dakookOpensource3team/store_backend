package com.example.ddd_start.member.applicaiton;

import com.example.ddd_start.common.domain.Address;
import com.example.ddd_start.member.applicaiton.model.AddressCommand;
import com.example.ddd_start.member.applicaiton.model.joinCommand;
import com.example.ddd_start.member.applicaiton.model.joinResponse;
import com.example.ddd_start.member.domain.Member;
import com.example.ddd_start.member.domain.MemberRepository;
import com.example.ddd_start.member.domain.Password;
import com.example.ddd_start.member.domain.PasswordEncryptionEngine;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public void findMemberByName(String name) {
    Sort sort = Sort.by("id").descending();

    PageRequest pageRequest = PageRequest.of(0, 10, sort);

    List<Member> result = memberRepository.findMemberByNameLike(name, pageRequest);
    result.forEach(
        e -> log.info("member Address: " + e.getAddress())
    );
  }

  @Transactional(readOnly = true)
  public void findPageMemberByName(String name) {
    PageRequest pageRequest = PageRequest.of(0, 10);

    Page<Member> result = memberRepository.findPageMemberByNameLike(name,
        pageRequest);
  }

  @Transactional
  public void blockMembers(Long[] blockingIds) {
    if (blockingIds == null | blockingIds.length == 0) {
      return;
    }

    List<Member> members = memberRepository.findByIdIn(blockingIds);
    for (Member member : members) {
      member.block();
    }
  }

}
