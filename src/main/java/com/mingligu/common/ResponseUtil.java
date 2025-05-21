package com.mingligu.common;

/**
 * 返回工具类
 *
 * @author tangtaiming
 * @version 1.0
 * @date 2025年03月21日
 */
public class ResponseUtil {

    /**
     * 成功
     * @param data
     * @return
     */
    public static <T> ResponseBase<T> ok(T data) {
        ResponseBase<T> responseBase = new ResponseBase<>(0, "ok", null, data);
        return responseBase;
    }

    /**
     * 成功
     * @return
     */
    public static <T> ResponseBase<T> ok() {
        ResponseBase<T> responseBase = new ResponseBase<>(0, "ok", null, null);
        return responseBase;
    }

    /**
     * 失败
     * @param code
     * @param msg
     * @return
     */
    public static <T> ResponseBase<T> fail(int code, String msg) {
//        ResponseBase<T> responseBase = new ResponseBase<>(code, msg, null, null);
        return fail(code, msg, null);
    }

    /**
     * 失败
     * @param code
     * @param msg
     * @param description
     * @return
     * @param <T>
     */
    public static <T> ResponseBase<T> fail(int code, String msg, String description) {
        ResponseBase<T> responseBase = new ResponseBase<>(code, msg, description, null);
        return responseBase;
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> ResponseBase<T> fail(ErrorCode errorCode) {
        ResponseBase<T> responseBase = new ResponseBase<>(errorCode.getCode(), errorCode.getMessage(), errorCode.getDescription(), null);
        return responseBase;
    }

    /**
     * 失败
     * @param errorCode
     * @param description
     * @return
     * @param <T>
     */
    public static <T> ResponseBase<T> fail(ErrorCode errorCode, String description) {
        ResponseBase<T> responseBase = new ResponseBase<>(errorCode.getCode(), errorCode.getMessage(), description, null);
        return responseBase;
    }
}
