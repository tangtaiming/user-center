package com.mingligu.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingligu.mapper.UserMapper;
import com.mingligu.model.User;
import com.mingligu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
* @author fr_tangtaiming
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-02-23 23:40:36
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    // 定义允许的字符集：仅字母和数字
    private static final String USERNAME_PATTERN = "[^a-zA-Z0-9]";

    //盐
    private static final String SALT = "mingligu";

    //用户登录态
    private static final String USER_LOGIN_STATE = "user_login_state";

    public UserServiceImpl(UserMapper userMapper) {
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            //todo 目前返回一个 -1 代表失败
            return -1;
        }
        if (userAccount.length() < 4) {
            //todo 长度小于4 返回一个 -1 代表失败
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            //todo 密码长度小于8 返回 -1 代表失败
            return -1;
        }
        //账户校验特殊字符
        if (validateExitUserAccount(userAccount)) {
            //todo 存在特殊字符 返回 -1 代表失败
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> wh = new QueryWrapper<>();
        wh.eq("userAccount", userAccount);
        long dbCount = this.baseMapper.selectCount(wh);
        if (dbCount > 0) {
            //todo 账户存在 返回-1 代表失败
            return -1;
        }
        //密码和校验密码是否相同
        if (!StringUtils.equals(userPassword, checkPassword)) {
            //todo 密码和校验密码不相同 返回 -1 代表失败
            return -1;
        }
        //2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3、插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean result = this.save(user);
        if (!result) {
            //插入失败 返回-1 代表失败
            return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 修改为自定义异常
            return null;
        }
        if (userAccount.length() < 4) {
            // todo 修改为自定义异常
            return null;
        }
        if (userPassword.length() < 8) {
            // todo 修改为自定义异常
            return null;
        }
        //账户校验特殊字符
        if (validateExitUserAccount(userAccount)) {
            // todo 修改为自定义异常
            return null;
        }

        //2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> wh = new QueryWrapper<>();
        wh.eq("userAccount", userAccount);
        wh.eq("userPassword", newPassword);
        User user = this.baseMapper.selectOne(wh);
        //用户不存在
        if (null == user) {
            log.info("user login failed, userAccount cannot match userPassword");
            // todo 修改为自定义异常
            return null;
        }

        //3、用户信息脱敏
        User clearUser = new User();
        clearUser.setUserAccount(user.getUserAccount());
        clearUser.setUserName(user.getUserName());
        clearUser.setAvatar(user.getAvatar());
        clearUser.setGender(user.getGender());
        clearUser.setPhone(user.getPhone());
        clearUser.setEmail(user.getEmail());
        clearUser.setUserStatus(user.getUserStatus());
        //4、session中存储用户信息
        request.getSession().setAttribute(USER_LOGIN_STATE, clearUser);

        return user;
    }

    /**
     * 验证用户账户是否存在特殊字符
     *
     * @param val  验证值
     * @return   true 存在特殊字符 / false 不存在特殊字符
     */
    public boolean validateExitUserAccount(String val) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(val);
        return matcher.find();
    }

}




