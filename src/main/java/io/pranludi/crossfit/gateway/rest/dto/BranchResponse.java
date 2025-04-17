package io.pranludi.crossfit.gateway.rest.dto;

public record BranchResponse(
    String code,
    String message,
    BranchResult result
) {

}
