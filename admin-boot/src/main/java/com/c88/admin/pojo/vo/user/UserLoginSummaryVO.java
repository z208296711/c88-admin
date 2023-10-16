package com.c88.admin.pojo.vo.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;


@ApiModel("用户分页视图对象")
@Data
public class UserLoginSummaryVO {

    /**
     * 用户名
     */
    private Long userId;


    /**
     * 用户名
     */
    private String username;

    /**
     * 登入IP
     */
    private String lastLoginIp;

    /**
     * 创建时间
     */
    private Integer loginCount;

}
