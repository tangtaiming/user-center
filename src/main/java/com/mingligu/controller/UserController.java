package com.mingligu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mingligu.constant.UserContact;
import com.mingligu.model.User;
import com.mingligu.model.request.UserLoginRequest;
import com.mingligu.model.request.UserRegisterRequest;
import com.mingligu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户接口
 *
 * @author tangtaiming
 * @version 1.0
 * @date 2025年03月02日
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param request 请求用户注册信息
     * @return   注册成功 返回注册id，失败返回 null
     */
    @RequestMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest request) {
        if (null == request) {
            return null;
        }

        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录信息
     * @param request          http请求 request
     * @return   成功返回用户信息，失败返回 null
     */
    @PostMapping(value = "/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (null == userLoginRequest) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 查询用户
     * @param userName 用户名
     * @return 返回用户数据集合
     */
    @GetMapping("/search")
    public List<User> searchUsers(String userName, HttpServletRequest request) {
        if (isDefault(request)) {
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }

        return userService.list(queryWrapper);
    }

    /**
     * 用户删除
     * @param id 用户id
     * @return true 删除用户 / false 删除失败
     */
    @PostMapping("/delete")
    public boolean deleteUser(long id, HttpServletRequest request) {
        if (isDefault(request)) {
            return false;
        }

        if (id <= 0) {
            return false;
        }

        return userService.removeById(id);
    }

    /**
     * 是否是普通用户
     * @param request Http请求对象
     * @return 默认成员 true / 管理员 false
     */
    private boolean isDefault(HttpServletRequest request) {
        return !isAdmin(request);
    }

    /**
     * 是否是管理员
     * @param request Http请求对象
     * @return  管理员 true / 默认成员 false
     */
    private boolean isAdmin(HttpServletRequest request) {
        //仅超级管理员用户可以查询
        Object objUser = request.getSession().getAttribute(UserContact.USER_LOGIN_STATE);
        User user = (User) objUser;
        if (null == user || user.getUserRole() != UserContact.ADMIN_ROLE) {
            return false;
        }
        return true;
    }

}
