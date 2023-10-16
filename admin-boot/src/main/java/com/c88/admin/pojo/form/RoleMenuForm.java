package com.c88.admin.pojo.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema(title = "角色變更menu")
public class RoleMenuForm {

    @Schema(title = "菜單ID")
    private List<Long> menuIds;

}
