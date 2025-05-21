package com.mingligu.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求实体
 *
 * @author tangtaiming
 * @version 1.0
 * @date 2025年03月02日
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户账户
    private String userAccount;

    //用户密码
    private String userPassword;

    //确认密码
    private String checkPassword;

    //邀请码
    private String inviteCode;

}
