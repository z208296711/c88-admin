package com.c88.admin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum UploadTypeEnum {

    IMAGE(0, "圖片"),
    FREE(1, "不限制");

    private final Integer code;

    private final String desc;

    public static UploadTypeEnum getEnum(Integer code) {
        return Arrays.stream(values()).filter(filter -> Objects.equals(filter.getCode(), code)).findFirst().orElseThrow();
    }

}
