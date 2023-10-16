package com.c88.admin.pojo.query;

import com.c88.common.core.base.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class PermissionPageQuery extends BasePageQuery {

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("菜单ID")
    private Long menuId;

}
