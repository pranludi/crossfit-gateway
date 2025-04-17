package io.pranludi.crossfit.gateway.rest;

import io.pranludi.crossfit.gateway.client.grpc.GrpcBranchClient;
import io.pranludi.crossfit.gateway.rest.dto.BranchesResponse;
import io.pranludi.crossfit.gateway.rest.dto.BranchRequest;
import io.pranludi.crossfit.gateway.rest.dto.BranchResponse;
import io.pranludi.crossfit.gateway.rest.mapper.GrpcMapper;
import io.pranludi.crossfit.protobuf.branch.AllBranchesResponse;
import io.pranludi.crossfit.protobuf.branch.GetBranchResponse;
import io.pranludi.crossfit.protobuf.branch.SaveBranchResponse;
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
@RequestMapping("/branch")
public class BranchController {

    final Logger log = LoggerFactory.getLogger(BranchController.class);

    final GrpcBranchClient grpcBranchClient;

    public BranchController(GrpcBranchClient grpcBranchClient) {
        this.grpcBranchClient = grpcBranchClient;
    }

    @PostMapping("/saveBranch")
    public ResponseEntity<BranchResponse> saveMember(@RequestBody BranchRequest req) {
        SaveBranchResponse response = grpcBranchClient.saveBranch(req.id(), req.password(), req.name(), req.email(), req.phoneNumber());
        BranchResponse branchResponse = GrpcMapper.INSTANCE.makeBranchResponse(
            response.getCode(),
            response.getMessage(),
            response.getResult().getBranch()
        );
        return ResponseEntity.ok(branchResponse);
    }

    @GetMapping("/getBranch")
    public ResponseEntity<BranchResponse> getBranch(@RequestParam String id) {
        GetBranchResponse response = grpcBranchClient.getBranchById(id);
        BranchResponse branchResponse = GrpcMapper.INSTANCE.makeBranchResponse(
            response.getCode(),
            response.getMessage(),
            response.getResult().getBranch()
        );
        return ResponseEntity.ok(branchResponse);
    }

    @GetMapping("/allBranches")
    public ResponseEntity<BranchesResponse> allBranches(@RequestParam String id) {
        AllBranchesResponse response = grpcBranchClient.allBranches(id);
        BranchesResponse branchesResponse = GrpcMapper.INSTANCE.makeBranchesResponse(
            response.getCode(),
            response.getMessage(),
            response.getResult().getBranchesList()
        );
        return ResponseEntity.ok(branchesResponse);
    }

}
