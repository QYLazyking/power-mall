package cn.lazyking.constants;

import lombok.Getter;

@Getter
public enum BusinessStatus {
    OPERATION_FAIL(-1, "操作失败"),
    SERVER_INNER_ERROR(9999, "服务器内部错误"),
    UNAUTHORIZED(401, "未授权"),
    ;

    private final int code;
    private final String msg;

    BusinessStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
