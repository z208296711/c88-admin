package com.c88.admin.pojo.vo.user;

import com.c88.admin.pojo.vo.permission.RoleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;


@Schema(name = "員工資訊資料結構-分頁")
@Data
public class UserPageVO {

    @Schema(name = "編號")
    private Long id;

    @Schema(name = "管理員名稱")
    private String username;

    @Schema(name = "所屬角色")
    private List<RoleVO> roleList = Collections.emptyList();

    @Schema(name = "最後登入IP")
    private String lastLoginIp = "";

    @Schema(name = "登入次數")
    private Integer loginCount = 0;

}
