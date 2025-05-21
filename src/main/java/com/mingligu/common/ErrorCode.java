package com.mingligu.common;

/**
 * 错误码
 *
 * @author tangtaiming
 * @version 1.0
 * @date 2025年05月21日
 */
public enum ErrorCode {

    SUCESSED(0, "成功", "success"),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    PARAMS_NULL_ERROR(40001, "数据为空", ""),
    NO_AUTH(41000, "无权限", ""),
    NOT_LOGIN(41001, "未登录", ""),
    DATA_IS_EXIST(41002, "数据存在", ""),
    SYSTEM_ERROR(50000, "系统异常", ""),
    ;


    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
