package io.pranludi.crossfit.gateway.client;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class GrpcMemberServiceMetadata extends CallCredentials {

    private String memberId;
    private String token;
    private Metadata.Key<String> MD_MEMBER_ID = Metadata.Key.of("member-id", Metadata.ASCII_STRING_MARSHALLER);
    private Metadata.Key<String> MD_TOKEN = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER);

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
