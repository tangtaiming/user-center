package com.mingligu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingligu.mapper.UserMapper;
import com.mingligu.model.User;
import com.mingligu.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author fr_tangtaiming
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-02-23 23:40:36
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




