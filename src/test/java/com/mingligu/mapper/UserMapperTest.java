package com.mingligu.mapper;
import java.util.Date;

import com.mingligu.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 添加用户Mapper测试类
 * @author tangtaiming
 * @version 1.0
 * @date 2025年02月26日
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;



    @Test
    void testAddUser() {
        User user = new User();
        user.setUserAccount("mingligu");
        user.setUserName("明里咕");
        user.setAvatar("http://xx.jpg");
        user.setGender(0);
        user.setUserPassword("12345678");
        user.setPhone("15211636823");
        user.setEmail("xx@qq.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        userMapper.insert(user);
    }


}