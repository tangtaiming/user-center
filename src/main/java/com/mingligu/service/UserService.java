package com.mingligu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingligu.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
* @author fr_tangtaiming
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-02-23 23:40:36
*/
public interface UserService extends IService<User> {

    /**
     * 查询用户列表
     * @param current
     * @param pageSize
     * @param request
     * @return
     */
    List<User> list(Integer current, Integer pageSize, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param  inviteCode  邀请码
     * @return 1 成功， 0 失败
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String inviteCode);

    /**
     * 用户登录
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request  http request 请求信息
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser  原用户
     * @return  脱敏用户
     */
    User getSafetyUser(User originUser);

    /**
     * 注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
