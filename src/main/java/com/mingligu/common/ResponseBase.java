package com.mingligu.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用接口返回对象
 *
 * @author tangtaiming
 * @version 1.0
 * @date 2025年05月19日
 */
@Data
public class ResponseBase<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    /**
     * 错误详情
     */
    private String description;

    public ResponseBase(int code, String message, String description, T data) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
    }

    public ResponseBase(int code, String message) {
        this(code, message, null,null);
    }

    /**
     * 错误返回
     * @param errorCode
     */
    public ResponseBase(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null, null);
    }

}
