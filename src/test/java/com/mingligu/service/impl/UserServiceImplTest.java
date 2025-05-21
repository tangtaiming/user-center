package com.mingligu.service.impl;

import com.mingligu.UserCenterApplication;
import com.mingligu.model.User;
import com.mingligu.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 用户服务测试
 * @author tangtaiming
 * @version 1.0
 * @date 2025年02月27日
 */
@SpringBootTest(classes = UserCenterApplication.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void userListTest() {
        List<User> userList = userService.list(1, 10, null);
        Assertions.assertNotNull(userList);
    }

    @Test
    public void userRegister() {
        //非空校验
        String accountAccount = "mingligu";
        String accountPassword = "";
        String checkPassword = "123456";
        String inviteCode = "user123";
        long re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertEquals(-1, re);
        //账户小于4位
        accountAccount = "mlg";
        re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertEquals(-1, re);
        //密码小于8位
        accountPassword = "x123456";
        re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertEquals(-1, re);
        //账户重复
        accountAccount = "mingligu";
        re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertEquals(-1, re);
        //账户包含特殊字符
        accountPassword = "x 123456";
        re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertEquals(-1, re);
        //密码和校验密码不同
        accountPassword = "12345678";
        checkPassword = "123456xx";
        re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertEquals(-1, re);
        //正确插入
        accountAccount = "mingligu2024";
        accountPassword = "12345678";
        checkPassword = "12345678";
        re = userService.userRegister(accountAccount, accountPassword, checkPassword, inviteCode);
        Assertions.assertTrue(re > 0);
    }
}