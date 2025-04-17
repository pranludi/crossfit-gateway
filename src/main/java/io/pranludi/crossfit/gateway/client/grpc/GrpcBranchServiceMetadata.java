package io.pranludi.crossfit.gateway.client.grpc;

import static io.pranludi.crossfit.gateway.client.grpc.InterceptorConstant.MD_BRANCH_ID;
import static io.pranludi.crossfit.gateway.client.grpc.InterceptorConstant.MD_TOKEN;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class GrpcBranchServiceMetadata extends CallCredentials {

    final String branchId;
    final String token;

    public GrpcBranchServiceMetadata(String branchId, String token) {
        this.branchId = branchId;
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> {
            try {
                Metadata metadata = new Metadata();
                metadata.put(MD_BRANCH_ID, branchId);
                metadata.put(MD_TOKEN, token);
                metadataApplier.apply(metadata);
            } catch (Exception e) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });
    }
}
