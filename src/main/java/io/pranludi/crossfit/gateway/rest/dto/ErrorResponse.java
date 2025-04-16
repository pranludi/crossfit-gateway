package io.pranludi.crossfit.gateway.rest.dto;

public record ErrorResponse(
    int code,
    String message
) {

}
