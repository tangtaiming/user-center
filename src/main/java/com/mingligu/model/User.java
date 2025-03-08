package com.mingligu.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 登录账号
     */
    private String userAccount;

    /**
     * 昵称
     */
    private String userName;

    /**
     *
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 0-正常 、1-删除
     */
    private Integer isDelete;

    /**
     * 0 普通用户, 1 超级管理员
     */
    private Integer userRole;
}