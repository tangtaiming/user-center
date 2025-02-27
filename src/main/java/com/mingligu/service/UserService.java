package com.mingligu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingligu.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
* @author fr_tangtaiming
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-02-23 23:40:36
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 1 成功， 0 失败
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request  http request 请求信息
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

}
