package com.c88.admin.pojo.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NextRouteVO {

    private String path;

    private String component;

    private String redirect;

    private String name;

    private Meta meta;

    @Data
    public static class Meta {

        private String title;

        private String icon;

        private Boolean hidden;

        /**
         * 如果设置为 true，目录没有子节点也会显示
         */
        private Boolean alwaysShow;

        private List<String> roles;

        /**
         * 页面缓存开启状态
         */
        private Boolean keepAlive;
    }

    private List<NextRouteVO> children;
}
