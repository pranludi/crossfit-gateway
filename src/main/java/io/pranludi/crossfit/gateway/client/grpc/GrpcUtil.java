package io.pranludi.crossfit.gateway.client.grpc;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import java.util.function.Supplier;

public class GrpcUtil {

    public static <T> T circuitBreakerDecoration(
        CircuitBreaker grpcCircuitBreaker,
        Supplier<T> call,
        Supplier<T> fallback
    ) {
        Supplier<T> decoratedSupplier =
            CircuitBreaker.decorateSupplier(grpcCircuitBreaker, call
            );

        return Try.ofSupplier(decoratedSupplier)
            .recover(throwable -> fallback.get())
            .get();
    }

}
