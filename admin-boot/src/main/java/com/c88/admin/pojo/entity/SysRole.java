package com.c88.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.c88.common.core.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(name = "角色")
@Data
public class SysRole extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(title = "角色名稱")
    private String name;

    @Schema(title = "角色编码")
    private String code;

    private Integer sort;

    private Integer status;

    private String remark;

    @Schema(title = "逻辑删除标识 0-未删除 1-已删除")
    //@TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(exist = false)
    private List<Long> menuIds;

    @TableField(exist = false)
    private List<Long> permissionIds;

}
