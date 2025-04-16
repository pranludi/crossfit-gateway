package io.pranludi.crossfit.gateway.domain.server;

public enum ServerType {

    GATEWAY_SERVICE("GATEWAY_SERVICE", "Gateway 서비스"),
    MEMBER_SERVICE("MEMBER_SERVICE", "회원 서비스"),
    BOARD_SERVICE("BOARD_SERVICE", "게시판 서비스"),
    BRANCH_SERVICE("BRANCH_SERVICE", "지점 서비스");

    private final String code;
    private final String description;

    ServerType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
