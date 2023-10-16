package com.c88.admin.pojo.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteVO {

    private String path;

    private String component;

    private String redirect;

    /**
     * 如果设置为 true
     */
    private Boolean alwaysShow;

    private String name;

    private Boolean hidden;

    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String title;
        private String icon;
        private List<String> roles;
    }
    private List<RouteVO> children;
}
