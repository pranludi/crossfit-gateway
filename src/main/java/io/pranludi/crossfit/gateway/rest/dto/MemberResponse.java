package io.pranludi.crossfit.gateway.rest.dto;

public record MemberResponse(
    String name,
    String email,
    String phoneNumber,
    String grade
) {

}
