package io.pranludi.crossfit.gateway.client.grpc;

import io.grpc.Metadata;

public class InterceptorConstant {

    public static final Metadata.Key<String> MD_MEMBER_ID = Metadata.Key.of("member-id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> MD_BRANCH_ID = Metadata.Key.of("branch-id", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> MD_TOKEN = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER);

}
