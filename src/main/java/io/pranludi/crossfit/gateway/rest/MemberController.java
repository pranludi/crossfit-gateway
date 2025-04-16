package io.pranludi.crossfit.gateway.rest;

import io.pranludi.crossfit.gateway.client.grpc.GrpcMemberClient;
import io.pranludi.crossfit.member.protobuf.GetMemberResponse;
import io.pranludi.crossfit.member.protobuf.MemberDTO;
import io.pranludi.crossfit.member.protobuf.MemberGradeDTO;
import io.pranludi.crossfit.member.protobuf.SignUpResponse;
import java.util.Map;
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
    public ResponseEntity<MemberDTO> saveMember(
        @RequestBody Map<String, String> body
    ) {
        // todo mapstruct
        String id = body.get("id");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");
        String grade = body.get("grade");
        try {
            SignUpResponse response = grpcMemberClient.saveMember(id, password, name, email, MemberGradeDTO.valueOf(grade));
            return ResponseEntity.ok(response.getMember());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getMember")
    public ResponseEntity<MemberDTO> saveMember(@RequestParam String id) {
        try {
            GetMemberResponse response = grpcMemberClient.getMemberById(id);
            return ResponseEntity.ok(response.getMember());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
