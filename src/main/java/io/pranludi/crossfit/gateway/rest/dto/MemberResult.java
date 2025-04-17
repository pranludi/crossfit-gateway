package io.pranludi.crossfit.gateway.rest.dto;

public record MemberResult(
    String name,
    String email,
    String phoneNumber,
    String grade
) {

}
