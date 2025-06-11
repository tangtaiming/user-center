package com.mingligu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingligu.common.ErrorCode;
import com.mingligu.constant.UserContact;
import com.mingligu.exception.BusinessException;
import com.mingligu.mapper.UserMapper;
import com.mingligu.model.User;
import com.mingligu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> list(Integer current, Integer pageSize, HttpServletRequest request) {
        IPage<User> page = new Page<>(current, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userMapper.selectPage(page, queryWrapper);

        List<User> userList = page.getRecords();
        List<User> safetyList = new ArrayList<>();
        for (User user : userList) {
            User newUser = getSafetyUser(user);
            safetyList.add(newUser);
        }

        return safetyList;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String inviteCode) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            //todo 目前返回一个 -1 代表失败
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            //todo 长度小于4 返回一个 -1 代表失败
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            //todo 密码长度小于8 返回 -1 代表失败
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8");
        }
        //账户校验特殊字符
        if (validateExitUserAccount(userAccount)) {
            //todo 存在特殊字符 返回 -1 代表失败
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号非法");
        }
        if (!StringUtils.equals("user123", inviteCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邀请码错误");
        }
        //账户不能重复
        QueryWrapper<User> wh = new QueryWrapper<>();
        wh.eq("userAccount", userAccount);
        long dbCount = this.baseMapper.selectCount(wh);
        if (dbCount > 0) {
            //todo 账户存在 返回-1 代表失败
            throw new BusinessException(ErrorCode.DATA_IS_EXIST, "账号已存在");
        }
        //密码和校验密码是否相同
        if (!StringUtils.equals(userPassword, checkPassword)) {
            //todo 密码和校验密码不相同 返回 -1 代表失败
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不一致");
        }
        //2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3、插入用户数据
        Date currentDate = new Date(System.currentTimeMillis());

        User user = new User();
        user.setUserName("user" + currentDate.getTime());
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setInviteCode(inviteCode);
        user.setCreateTime(currentDate);
        user.setUpdateTime(currentDate);
        boolean result = this.save(user);
        if (!result) {
            //插入失败 返回-1 代表失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名长度小于4位!");
        }
        if (userPassword.length() < 8) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8位!");
        }
        //账户校验特殊字符
        if (validateExitUserAccount(userAccount)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名非法!");
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
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "账户名/密码错误!");
        }

        //3、用户信息脱敏
        User safteyUser = getSafetyUser(user);
        //4、session中存储用户信息
        request.getSession().setAttribute(UserContact.USER_LOGIN_STATE, safteyUser);

        return safteyUser;
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

    @Override
    public User getSafetyUser(User originUser) {
        if (null == originUser) {
            return null;
        }

        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setAvatar(originUser.getAvatar());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserContact.USER_LOGIN_STATE);
        return 1;
    }

}




