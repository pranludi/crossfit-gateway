package io.pranludi.crossfit.gateway.rest.dto;

public record BranchesResponse(
    String code,
    String message,
    BranchesResult result
) {

}
