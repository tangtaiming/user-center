package com.mingligu;

import com.mingligu.mapper.UserMapper;
import com.mingligu.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(userList.isEmpty());
//        userList.forEach(System.out::println);
    }

    @SneakyThrows
    @Test
    void testDigest() {
        String salt = "abc";
        String newPassword = DigestUtils.md5DigestAsHex((salt + "123456").getBytes());
        System.out.println(newPassword);
    }

}
