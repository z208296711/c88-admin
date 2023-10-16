package com.c88.admin.pojo.query;


import com.c88.common.core.base.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class UserPageQuery extends BasePageQuery {

    @ApiModelProperty("关键字(用户名、昵称、手机号)")
    private String keywords;

    @ApiModelProperty("用户状态")
    private Integer status;

    @ApiModelProperty("部门ID")
    private Long deptId;

}
