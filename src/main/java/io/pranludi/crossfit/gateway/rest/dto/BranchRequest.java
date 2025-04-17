package io.pranludi.crossfit.gateway.rest.dto;

public record BranchRequest(
    String id,
    String password,
    String name,
    String email,
    String phoneNumber
) {

}
