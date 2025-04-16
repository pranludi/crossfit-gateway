package io.pranludi.crossfit.gateway.rest;

import io.pranludi.crossfit.gateway.client.grpc.GrpcMemberClient;
import io.pranludi.crossfit.gateway.rest.dto.MemberRequest;
import io.pranludi.crossfit.gateway.rest.dto.MemberResponse;
import io.pranludi.crossfit.gateway.rest.mapper.GrpcMapper;
import io.pranludi.crossfit.member.protobuf.GetMemberResponse;
import io.pranludi.crossfit.member.protobuf.MemberGradeDTO;
import io.pranludi.crossfit.member.protobuf.SignUpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    final Logger log = LoggerFactory.getLogger(MemberController.class);

    final GrpcMemberClient grpcMemberClient;

    public MemberController(GrpcMemberClient grpcMemberClient) {
        this.grpcMemberClient = grpcMemberClient;
    }

    // HTTP 요청 -> gRPC 호출 -> 원격 gRPC 서버 -> 응답
    @PostMapping("/saveMember")
    public ResponseEntity<MemberResponse> saveMember(@RequestBody MemberRequest req) {
        try {
            SignUpResponse response = grpcMemberClient.saveMember(req.id(), req.password(), req.name(), req.email(), req.phoneNumber(), MemberGradeDTO.valueOf(req.grade()));
            MemberResponse memberResponse = GrpcMapper.INSTANCE.memberEntityToProto(response.getMember());
            return ResponseEntity.ok(memberResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getMember")
    public ResponseEntity<MemberResponse> saveMember(@RequestParam String id) {
        try {
            GetMemberResponse response = grpcMemberClient.getMemberById(id);
            MemberResponse memberResponse = GrpcMapper.INSTANCE.memberEntityToProto(response.getMember());
            return ResponseEntity.ok(memberResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
