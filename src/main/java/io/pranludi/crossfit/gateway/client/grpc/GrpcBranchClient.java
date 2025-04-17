package io.pranludi.crossfit.gateway.client.grpc;

import static io.pranludi.crossfit.gateway.client.grpc.GrpcUtil.circuitBreakerDecoration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.pranludi.crossfit.gateway.client.jwt.ServerTokenUtil;
import io.pranludi.crossfit.gateway.domain.server.ServerType;
import io.pranludi.crossfit.protobuf.ResultCode;
import io.pranludi.crossfit.protobuf.branch.AllBranchesRequest;
import io.pranludi.crossfit.protobuf.branch.AllBranchesResponse;
import io.pranludi.crossfit.protobuf.branch.BranchServiceGrpc;
import io.pranludi.crossfit.protobuf.branch.GetBranchRequest;
import io.pranludi.crossfit.protobuf.branch.GetBranchResponse;
import io.pranludi.crossfit.protobuf.branch.SaveBranchRequest;
import io.pranludi.crossfit.protobuf.branch.SaveBranchResponse;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GrpcBranchClient {

    final Logger log = LoggerFactory.getLogger(GrpcBranchClient.class);
    final ServerTokenUtil serverTokenUtil;
    final CircuitBreakerRegistry circuitBreakerRegistry;

    @GrpcClient("branch-service-url")
    Channel channel;

    CircuitBreaker grpcCircuitBreaker;
    BranchServiceGrpc.BranchServiceBlockingStub stub;

    public GrpcBranchClient(CircuitBreakerRegistry circuitBreakerRegistry, ServerTokenUtil serverTokenUtil) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.serverTokenUtil = serverTokenUtil;
    }

    @PostConstruct
    public void init() {
        grpcCircuitBreaker = circuitBreakerRegistry.circuitBreaker("branch-service-circuit");
        stub = BranchServiceGrpc.newBlockingStub(channel);
    }

    // 지점 등록
    public SaveBranchResponse saveBranch(
        String branchId,
        String password,
        String name,
        String email,
        String phoneNumber
    ) {
        Supplier<SaveBranchResponse> caller = () -> {
            try {
                SaveBranchRequest req = SaveBranchRequest.newBuilder()
                    .setPassword(password)
                    .setName(name)
                    .setEmail(email)
                    .setPhoneNumber(phoneNumber)
                    .build();

                var token = serverTokenUtil.generateServerToken(ServerType.BRANCH_SERVICE, "server_id");
                return stub
                    .withCallCredentials(new GrpcBranchServiceMetadata(branchId, token))
                    .saveBranch(req);
            } catch (StatusRuntimeException e) {
                log.error("[gRPC 호출 실패] 상태: {}, 설명: {}, 원인: {}",
                    e.getStatus(),
                    e.getStatus().getDescription(),
                    e.getCause());
                throw e;
            }
        };

        Supplier<SaveBranchResponse> fallback = () -> {
            log.error("[CircuitBreaker-Fallback] branchId : {}", branchId);
            return SaveBranchResponse.newBuilder()
                .setCode(ResultCode.CIRCUIT_BREAKER)
                .setMessage("[CircuitBreaker-Fallback]")
                .build();
        };

        return circuitBreakerDecoration(grpcCircuitBreaker, caller, fallback);
    }

    // ID로 조회
    public GetBranchResponse getBranchById(String branchId) {
        Supplier<GetBranchResponse> caller = () -> {
            try {
                GetBranchRequest req = GetBranchRequest.newBuilder().build();
                var token = serverTokenUtil.generateServerToken(ServerType.BRANCH_SERVICE, "server_id");
                return stub
                    .withCallCredentials(new GrpcBranchServiceMetadata(branchId, token))
                    .getBranch(req);
            } catch (StatusRuntimeException e) {
                log.error("[gRPC 호출 실패] 상태: {}, 설명: {}, 원인: {}",
                    e.getStatus(),
                    e.getStatus().getDescription(),
                    e.getCause());
                throw e;
            }
        };

        Supplier<GetBranchResponse> fallback = () -> {
            log.error("[CircuitBreaker-Fallback] branchId : {}", branchId);
            return GetBranchResponse.newBuilder()
                .setCode(ResultCode.CIRCUIT_BREAKER)
                .setMessage("[CircuitBreaker-Fallback]")
                .build();
        };

        return circuitBreakerDecoration(grpcCircuitBreaker, caller, fallback);
    }

    // 모든 지점 조회
    public AllBranchesResponse allBranches(String branchId) {
        Supplier<AllBranchesResponse> caller = () -> {
            try {
                AllBranchesRequest req = AllBranchesRequest.newBuilder().build();
                var token = serverTokenUtil.generateServerToken(ServerType.BRANCH_SERVICE, "server_id");
                return stub
                    .withCallCredentials(new GrpcBranchServiceMetadata(branchId, token))
                    .allBranches(req);
            } catch (StatusRuntimeException e) {
                log.error("[gRPC 호출 실패] 상태: {}, 설명: {}, 원인: {}",
                    e.getStatus(),
                    e.getStatus().getDescription(),
                    e.getCause());
                throw e;
            }
        };

        Supplier<AllBranchesResponse> fallback = () -> {
            log.error("[CircuitBreaker-Fallback] branchId : {}", branchId);
            return AllBranchesResponse.newBuilder()
                .setCode(ResultCode.CIRCUIT_BREAKER)
                .setMessage("[CircuitBreaker-Fallback]")
                .build();
        };

        return circuitBreakerDecoration(grpcCircuitBreaker, caller, fallback);
    }

}
