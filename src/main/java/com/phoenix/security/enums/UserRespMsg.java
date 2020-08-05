package com.phoenix.security.enums;

public enum UserRespMsg {
    NOT_EXIST("USER_404", "用户不存在");

    private String code;
    private String msg;

    private UserRespMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
