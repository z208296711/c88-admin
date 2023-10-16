package com.c88.admin.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.c88.common.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public enum MenuTypeEnum implements IBaseEnum<Integer> {
    NULL(0, "未知"),
    MENU(1, "菜单"),
    CATALOG(2, "目录"),
    EXTLINK(3, "外链"),
    PERMISSION(4, "權限");

    @Getter
    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private final Integer value;

    @Getter
    // @JsonValue //  表示对枚举序列化时返回此字段
    private final String label;

    MenuTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public static MenuTypeEnum getEnum(Integer code) {
        return Arrays.stream(values()).filter(filter -> Objects.equals(filter.getValue(), code)).findFirst().orElseThrow();
    }

}
