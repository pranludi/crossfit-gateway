package io.pranludi.crossfit.gateway.client.grpc;

import static io.pranludi.crossfit.gateway.client.grpc.InterceptorConstant.MD_MEMBER_ID;
import static io.pranludi.crossfit.gateway.client.grpc.InterceptorConstant.MD_TOKEN;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class GrpcMemberServiceMetadata extends CallCredentials {

    final String memberId;
    final String token;

    public GrpcMemberServiceMetadata(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> {
            try {
                Metadata metadata = new Metadata();
                metadata.put(MD_MEMBER_ID, memberId);
                metadata.put(MD_TOKEN, token);
                metadataApplier.apply(metadata);
            } catch (Exception e) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });
    }
}
