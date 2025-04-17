package io.pranludi.crossfit.gateway.client.grpc;

import static io.pranludi.crossfit.gateway.client.grpc.GrpcUtil.circuitBreakerDecoration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.pranludi.crossfit.gateway.client.jwt.ServerTokenUtil;
import io.pranludi.crossfit.gateway.domain.server.ServerType;
import io.pranludi.crossfit.protobuf.MemberGradeDTO;
import io.pranludi.crossfit.protobuf.ResultCode;
import io.pranludi.crossfit.protobuf.member.GetMemberRequest;
import io.pranludi.crossfit.protobuf.member.GetMemberResponse;
import io.pranludi.crossfit.protobuf.member.MemberServiceGrpc;
import io.pranludi.crossfit.protobuf.member.SignUpRequest;
import io.pranludi.crossfit.protobuf.member.SignUpResponse;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GrpcMemberClient {

    final Logger log = LoggerFactory.getLogger(GrpcMemberClient.class);
    final ServerTokenUtil serverTokenUtil;
    final CircuitBreakerRegistry circuitBreakerRegistry;

    @GrpcClient("member-service-url")
    Channel channel;

    CircuitBreaker grpcCircuitBreaker;
    MemberServiceGrpc.MemberServiceBlockingStub stub;

    public GrpcMemberClient(CircuitBreakerRegistry circuitBreakerRegistry, ServerTokenUtil serverTokenUtil) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.serverTokenUtil = serverTokenUtil;
    }

    @PostConstruct
    public void init() {
        grpcCircuitBreaker = circuitBreakerRegistry.circuitBreaker("member-service-circuit");
        stub = MemberServiceGrpc.newBlockingStub(channel);
    }

    // 회원 등록
    public SignUpResponse saveMember(
        String memberId,
        String password,
        String name,
        String email,
        String phoneNumber,
        MemberGradeDTO grade
    ) {
        Supplier<SignUpResponse> caller = () -> {
            try {
                SignUpRequest req = SignUpRequest.newBuilder()
                    .setPassword(password)
                    .setName(name)
                    .setEmail(email)
                    .setPhoneNumber(phoneNumber)
                    .setGrade(grade)
                    .build();

                var token = serverTokenUtil.generateServerToken(ServerType.MEMBER_SERVICE, "server_id");
                return stub
                    .withCallCredentials(new GrpcMemberServiceMetadata(memberId, token))
                    .signUp(req);
            } catch (StatusRuntimeException e) {
                log.error("[gRPC 호출 실패] 상태: {}, 설명: {}, 원인: {}",
                    e.getStatus(),
                    e.getStatus().getDescription(),
                    e.getCause());
                throw e;
            }
        };

        Supplier<SignUpResponse> fallback = () -> {
            log.error("[CircuitBreaker-Fallback] memberId : {}", memberId);
            return SignUpResponse.newBuilder()
                .setCode(ResultCode.CIRCUIT_BREAKER)
                .setMessage("[CircuitBreaker-Fallback]")
                .build();
        };

        return circuitBreakerDecoration(grpcCircuitBreaker, caller, fallback);
    }

    // 회원 ID로 회원 조회
    public GetMemberResponse getMemberById(String memberId) {
        Supplier<GetMemberResponse> caller = () -> {
            try {
                GetMemberRequest req = GetMemberRequest.newBuilder().build();
                var token = serverTokenUtil.generateServerToken(ServerType.MEMBER_SERVICE, "server_id");
                return stub
                    .withCallCredentials(new GrpcMemberServiceMetadata(memberId, token))
                    .getMember(req);
            } catch (StatusRuntimeException e) {
                log.error("[gRPC 호출 실패] 상태: {}, 설명: {}, 원인: {}",
                    e.getStatus(),
                    e.getStatus().getDescription(),
                    e.getCause());
                throw e;
            }
        };

        Supplier<GetMemberResponse> fallback = () -> {
            log.error("[CircuitBreaker-Fallback] memberId : {}", memberId);
            return GetMemberResponse.newBuilder()
                .setCode(ResultCode.CIRCUIT_BREAKER)
                .setMessage("[CircuitBreaker-Fallback]")
                .build();
        };

        return circuitBreakerDecoration(grpcCircuitBreaker, caller, fallback);
    }

}
