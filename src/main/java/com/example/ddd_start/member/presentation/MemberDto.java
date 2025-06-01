package com.example.ddd_start.member.presentation;

import com.example.ddd_start.common.domain.Address;

public record MemberDto(String username, String name, String email, Address address) {

}
