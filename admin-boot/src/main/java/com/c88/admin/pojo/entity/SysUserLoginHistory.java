package com.c88.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.c88.common.core.base.BaseEntity;
import lombok.Data;

/**
 * 用户登入紀錄表
 */
@TableName(value = "sys_user_login_history")
@Data
public class SysUserLoginHistory extends BaseEntity {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户Uid
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;

    /**
     * 登入IP
     */
    private String ip;

    /**
     * 瀏覽器
     */
    private String browser;

    /**
     * 作業系統
     */
    private String os;

    /**
     * 地區
     */
    private String area;

    /**
     * 城市
     */
    private String city;
}