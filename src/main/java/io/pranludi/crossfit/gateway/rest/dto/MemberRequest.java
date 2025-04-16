package io.pranludi.crossfit.gateway.rest.dto;

public record MemberRequest(
    String id,
    String password,
    String name,
    String email,
    String phoneNumber,
    String grade
) {

}
