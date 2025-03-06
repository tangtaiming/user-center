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

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
