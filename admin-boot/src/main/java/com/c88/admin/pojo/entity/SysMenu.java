package com.c88.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.c88.admin.common.enums.MenuTypeEnum;
import com.c88.common.core.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SysMenu extends BaseEntity {

    @TableId(type = IdType.NONE)
    private Long id;

    private Long parentId;

    private Long catalogId;

    private String name;

    private String icon;

    /**
     * 路由相对路径
     */
    private String path;

    private String i18n;

    /**
     * 组件绝对路径
     */
    private String component;

    private String urlPerm;

    private String btnPerm;

    private Integer sort;

    private Integer visible;

    private String redirect;

    @TableField(exist = false)
    private List<String> roles;

    /**
     * 菜单类型(1:菜单；2：目录；3：外链 4:權限)
     */
    @TableField(value = "type", typeHandler = EnumOrdinalTypeHandler.class)
    private MenuTypeEnum type;

    @TableField(exist = false)
    private boolean have;

    private String note;
}
