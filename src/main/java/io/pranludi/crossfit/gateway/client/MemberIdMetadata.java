package io.pranludi.crossfit.gateway.client;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class MemberIdMetadata extends CallCredentials {

    private String memberId;
    private Metadata.Key<String> MD_MEMBER_ID = Metadata.Key.of("member-id", Metadata.ASCII_STRING_MARSHALLER);

    public MemberIdMetadata(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> {
            try {
                Metadata metadata = new Metadata();
                metadata.put(MD_MEMBER_ID, memberId);
                metadataApplier.apply(metadata);
            } catch (Exception e) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });
    }
}
