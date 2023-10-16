package com.c88.admin.pojo.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema(title = "使用者Form")
public class UserForm {

    @Schema(title = "帳號ID （修改時必填")
    private Long id;

    @Schema(title = "帳號名稱")
    private String username;

    @Schema(title = "帳號密碼")
    private String password;

    @Schema(title = "用戶角色ID")
    private List<Long> roleIds;

}
