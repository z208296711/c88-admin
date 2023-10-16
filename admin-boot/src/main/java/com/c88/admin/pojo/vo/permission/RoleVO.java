package com.c88.admin.pojo.vo.permission;

import cn.hutool.core.bean.BeanUtil;
import com.c88.admin.pojo.entity.SysRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(title = "角色VO")
public class RoleVO implements Serializable {

    @Schema(title = "編號ID 修改時必填")
    private Long id;

    @Schema(title = "角色名稱")
    private String name;

    @JsonIgnore
    @Schema(title = "角色编码", hidden = true)
    private String code;

    @Schema(title = "備註")
    private String remark;

    @Schema(title = "是否有對應權限")
    private boolean have;

    public RoleVO(SysRole sysRole) {
        BeanUtil.copyProperties(sysRole, this);
    }

    public RoleVO() {

    }

}
