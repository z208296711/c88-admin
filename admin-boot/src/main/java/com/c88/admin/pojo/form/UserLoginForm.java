package com.c88.admin.pojo.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "新增操作者登入紀錄")
@Data
public class UserLoginForm {

    /**
     * 用户Id
     */
    @Schema(title = "用户Id")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    private String username;

    /**
     * 登入IP
     */
    @Schema(title = "登入IP")
    private String ip;

    /**
     * 瀏覽器
     */
    @Schema(title = "瀏覽器")
    private String browser;

    /**
     * 作業系統
     */
    @Schema(title = "作業系統")
    private String os;

    /**
     * 地區
     */
    @Schema(title = "地區")
    private String area;

    /**
     * 城市
     */
    @Schema(title = "城市")
    private String city;

}
