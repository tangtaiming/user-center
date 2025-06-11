package com.mingligu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mingligu.common.ErrorCode;
import com.mingligu.common.ResponseBase;
import com.mingligu.constant.UserContact;
import com.mingligu.exception.BusinessException;
import com.mingligu.model.User;
import com.mingligu.model.request.UserLoginRequest;
import com.mingligu.model.request.UserRegisterRequest;
import com.mingligu.service.UserService;
import com.mingligu.common.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 管理用户列表
     * @param current
     * @param pageSize
     * @return
     */
    @PostMapping("/list")
    public Map<String, Object> userList(Integer current, Integer pageSize, HttpServletRequest request) {
        if (isDefault(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<User> userList = userService.list(current, pageSize, request);

        Map<String, Object> userVo = new HashMap<>();
        userVo.put("data", userList);
        userVo.put("success", true);
        userVo.put("total", 4);
        userVo.put("page", current);

        return userVo;
    }

    /**
     * 用户注册
     *
     * @param request 请求用户注册信息
     * @return   注册成功 返回注册id，失败返回 null
     */
    @RequestMapping("/register")
    public ResponseBase<Long> userRegister(@RequestBody UserRegisterRequest request) {
        if (null == request) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }

        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();
        String inviteCode = request.getInviteCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, inviteCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long registerId = userService.userRegister(userAccount, userPassword, checkPassword, inviteCode);
        return ResponseUtil.ok(registerId);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录信息
     * @param request          http请求 request
     * @return   成功返回用户信息，失败返回 null
     */
    @PostMapping(value = "/login")
    public ResponseBase<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (null == userLoginRequest) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResponseUtil.ok(user);
    }

    /**
     * 查询用户
     * @param userName 用户名
     * @return 返回用户数据集合
     */
    @GetMapping("/search")
    public ResponseBase<List<User>> searchUsers(String userName, HttpServletRequest request) {
        if (isDefault(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }

        List<User> userList = userService.list(queryWrapper);
        List<User> safetyUserList = userList
                .stream()
                .map(it -> userService.getSafetyUser(it))
                .collect(Collectors.toList());

        return ResponseUtil.ok(safetyUserList);
    }

    /**
     * 根据id 查询单签用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public ResponseBase<User> getCurrentUser(HttpServletRequest request) {
        Object objUser = request.getSession().getAttribute(UserContact.USER_LOGIN_STATE);
        User currentUser = (User) objUser;
        if (null == currentUser) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        //todo 校验用户是否合法
        User user = userService.getById(currentUser.getId());
        User safetyUser = userService.getSafetyUser(user);

        return ResponseUtil.ok(safetyUser);
    }

    /**
     * 用户删除
     * @param id 用户id
     * @return true 删除用户 / false 删除失败
     */
    @PostMapping("/delete")
    public ResponseBase<Boolean> deleteUser(long id, HttpServletRequest request) {
        if (isDefault(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        ResponseBase responseBase = null;
        boolean userDelete = userService.removeById(id);
        if (!userDelete) {
            responseBase = ResponseUtil.fail(40001, "删除用户失败");
            return responseBase;
        }

        responseBase = ResponseUtil.ok(userDelete);
        return responseBase;
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public ResponseBase<Boolean> userLogout(HttpServletRequest request) {
        userService.userLogout(request);
        return ResponseUtil.ok();
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
        if (objUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        User user = (User) objUser;
        if (null == user || user.getUserRole() != UserContact.ADMIN_ROLE) {
            return false;
        }
        return true;
    }

}
