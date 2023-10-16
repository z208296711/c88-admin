package com.c88.admin.pojo.query;


import com.c88.common.core.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(title = "登入紀錄查詢參數")
public class UserLoginPageQuery extends BasePageQuery {

    @Schema(title = "管理者帳號")
    private String username;

    @Schema(title = "最後登入IP")
    private String ip;

    @Schema(title = "开始时间(格式：yyyy-mm-ss hh:mm:ss)")
    private String beginDate;

    @Schema(title = "截止时间(格式：yyyy-mm-ss hh:mm:ss)")
    private String endDate;

}
