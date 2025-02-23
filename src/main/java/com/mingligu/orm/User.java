package com.mingligu.orm;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author tangtaiming
 * @version 1.0
 * @date 2025年02月23日
 */
@Data
@TableName("`user`")
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String email;

}
