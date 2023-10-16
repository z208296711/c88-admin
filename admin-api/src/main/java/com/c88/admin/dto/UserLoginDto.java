package com.c88.admin.dto;

import lombok.Data;


@Data
public class UserLoginDto {


    /**
     * 用戶id
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
