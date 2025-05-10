package com.example.ddd_start.member.applicaiton.model;

public record UpdateCommand(Long id, String email, String username, AddressCommand addressReq) {

}
