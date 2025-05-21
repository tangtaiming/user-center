package com.mingligu.exception;

import com.mingligu.common.ErrorCode;
import com.mingligu.common.ResponseBase;
import com.mingligu.common.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author tangtaiming
 * @version 1.0
 * @date 2025年05月21日
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseBase bussinessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.fail(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseBase runtimeHandler(RuntimeException runtimeException) {
        log.error(runtimeException.getMessage(), runtimeException);
        return ResponseUtil.fail(ErrorCode.SYSTEM_ERROR, runtimeException.getMessage());
    }

}
