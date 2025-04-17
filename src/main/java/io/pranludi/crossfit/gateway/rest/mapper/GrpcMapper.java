package io.pranludi.crossfit.gateway.rest.mapper;

import com.google.protobuf.Timestamp;
import io.pranludi.crossfit.gateway.rest.dto.BranchResponse;
import io.pranludi.crossfit.gateway.rest.dto.BranchResult;
import io.pranludi.crossfit.gateway.rest.dto.BranchesResponse;
import io.pranludi.crossfit.gateway.rest.dto.BranchesResult;
import io.pranludi.crossfit.gateway.rest.dto.MemberResponse;
import io.pranludi.crossfit.gateway.rest.dto.MemberResult;
import io.pranludi.crossfit.protobuf.BranchDTO;
import io.pranludi.crossfit.protobuf.MemberDTO;
import io.pranludi.crossfit.protobuf.ResultCode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrpcMapper {

    GrpcMapper INSTANCE = Mappers.getMapper(GrpcMapper.class);

    default Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
        Instant instant = java.sql.Timestamp.valueOf(localDateTime).toInstant();
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).build();
    }

    MemberResult memberEntityToProto(MemberDTO member);

    BranchResult branchEntityToProto(BranchDTO branch);

    default MemberResponse makeMemberResponse(ResultCode code, String message, MemberDTO member) {
        return new MemberResponse(code.name(), message, memberEntityToProto(member));
    }

    default BranchResponse makeBranchResponse(ResultCode code, String message, BranchDTO branch) {
        return new BranchResponse(code.name(), message, branchEntityToProto(branch));
    }

    default BranchesResponse makeBranchesResponse(ResultCode code, String message, List<BranchDTO> branches) {
        List<BranchResult> results = new ArrayList<>();
        for (BranchDTO branch : branches) {
            results.add(branchEntityToProto(branch));
        }
        return new BranchesResponse(code.name(), message, new BranchesResult(results));
    }

}
