package io.pranludi.crossfit.gateway.rest.dto;

import java.util.List;

public record BranchesResult(
    List<BranchResult> branches
) {

}
