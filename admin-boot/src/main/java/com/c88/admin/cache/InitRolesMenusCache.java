package com.c88.admin.cache;

import com.c88.admin.service.ISysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成时加载角色权限规则至Redis缓存
 */
@Component
@AllArgsConstructor
public class InitRolesMenusCache implements CommandLineRunner {

    private ISysMenuService iSysMenuService;

    @Override
    public void run(String... args) {
        iSysMenuService.refreshPermRolesRules();
    }
}
