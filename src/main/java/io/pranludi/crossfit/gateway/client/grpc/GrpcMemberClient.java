package io.pranludi.crossfit.gateway.client.grpc;

import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.pranludi.crossfit.gateway.client.GrpcMemberServiceMetadata;
import io.pranludi.crossfit.gateway.common.ServerTokenUtil;
import io.pranludi.crossfit.gateway.domain.server.ServerType;
import io.pranludi.crossfit.member.protobuf.GetMemberRequest;
import io.pranludi.crossfit.member.protobuf.GetMemberResponse;
import io.pranludi.crossfit.member.protobuf.MemberGradeDTO;
import io.pranludi.crossfit.member.protobuf.MemberServiceGrpc;
import io.pranludi.crossfit.member.protobuf.SignUpRequest;
import io.pranludi.crossfit.member.protobuf.SignUpResponse;
import javax.annotation.PostConstruct;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GrpcMemberClient {

    final Logger log = LoggerFactory.getLogger(GrpcMemberClient.class);
    final ServerTokenUtil serverTokenUtil;

    @GrpcClient("member-service")
    Channel channel;

    MemberServiceGrpc.MemberServiceBlockingStub stub;

    public GrpcMemberClient(ServerTokenUtil serverTokenUtil) {
        this.serverTokenUtil = serverTokenUtil;
    }

    @PostConstruct
    public void init() {
        stub = MemberServiceGrpc.newBlockingStub(channel);
    }

    // 회원 등록
    public SignUpResponse saveMember(
        String memberId,
        String password,
        String name,
        String email,
        MemberGradeDTO grade
    ) {
        try {
            // todo mapstruct
            SignUpRequest req = SignUpRequest.newBuilder()
                .setPassword(password)
                .setName(name)
                .setEmail(email)
                .setGrade(grade)
                .build();

            var token = serverTokenUtil.generateServerToken(ServerType.MEMBER_SERVICE, "server_id");

            return stub
                .withCallCredentials(new GrpcMemberServiceMetadata(memberId, token))
                .signUp(req);
        } catch (StatusRuntimeException e) {
            log.error("gRPC 호출 실패 - 상태: {}, 설명: {}, 원인: {}",
                e.getStatus(),
                e.getStatus().getDescription(),
                e.getCause());
            throw e;
        }
    }

    // 회원 ID로 회원 조회
    public GetMemberResponse getMemberById(String memberId) {
        try {
            GetMemberRequest req = GetMemberRequest.newBuilder().build();
            var token = serverTokenUtil.generateServerToken(ServerType.MEMBER_SERVICE, "server_id");
            return stub
                .withCallCredentials(new GrpcMemberServiceMetadata(memberId, token))
                .getMember(req);
        } catch (StatusRuntimeException e) {
            log.error("gRPC 호출 실패 - 상태: {}, 설명: {}, 원인: {}",
                e.getStatus(),
                e.getStatus().getDescription(),
                e.getCause());
            throw e;
        }
    }

}
