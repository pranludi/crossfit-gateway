package io.pranludi.crossfit.gateway.rest.dto;

public record MemberResponse(
    String code,
    String message,
    MemberResult result
) {

}
