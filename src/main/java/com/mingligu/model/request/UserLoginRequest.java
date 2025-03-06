package com.mingligu.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author tangtaiming
 * @version 1.0
 * @date 2025年03月02日
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

}
