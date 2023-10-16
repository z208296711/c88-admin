package com.c88.admin.pojo.vo.menu;

import com.c88.admin.common.enums.MenuTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class MenuTableVO {

    private Long id;

    private Long parentId;

    private String name;

    private String icon;

    private String i18n;

    private Integer sort;

    private MenuTypeEnum type;

    private boolean have;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<MenuTableVO> children;

}
