package com.c88.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.SysRoleMenuMapper;
import com.c88.admin.pojo.entity.SysRoleMenu;
import com.c88.admin.service.ISysRoleMenuService;
import com.c88.admin.service.ISysUserService;
import com.c88.feign.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    private final ISysUserService iSysUserService;

    private final AuthFeignClient authFeignClient;


    @Override
    public List<Long> listMenuIds(Long roleId) {
        return this.baseMapper.listMenuIds(roleId);
    }

    @Override
    @Transactional
    public boolean update(Long roleId, List<Long> menuIds) {
        boolean result = true;
        List<Long> dbMenuIds = this.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        // 删除数据库存在此次提交不存在的
        if (CollectionUtil.isNotEmpty(dbMenuIds)) {
            List<Long> removeMenuIds = dbMenuIds.stream().filter(id -> !menuIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removeMenuIds)) {
                this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)
                        .in(SysRoleMenu::getMenuId, removeMenuIds));
            }
        }

        // 插入数据库不存在的
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<Long> insertMenuIds = menuIds.stream().filter(id -> !dbMenuIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(insertMenuIds)) {
                List<SysRoleMenu> roleMenus = new ArrayList<>();
                for (Long insertMenuId : insertMenuIds) {
                    SysRoleMenu roleMenu = new SysRoleMenu().setRoleId(roleId).setMenuId(insertMenuId);
                    roleMenus.add(roleMenu);
                }
                result = this.saveBatch(roleMenus);
            }
        }

        List<String> usernameList = iSysUserService.getUserByRoleId(roleId);
        usernameList.forEach(name -> authFeignClient.cleanToken("c88-admin-web", name));


        return result;
    }

    @Override
    @Transactional
    public boolean update(Set<Long> roleIds, Long menuId) {
        boolean result = true;

        if (menuId != null) {
            this.remove(new LambdaQueryWrapper<SysRoleMenu>()
                    .eq(SysRoleMenu::getMenuId, menuId));
        }

        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<SysRoleMenu> roleMenus = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysRoleMenu roleMenu = new SysRoleMenu().setRoleId(roleId).setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            result = this.saveBatch(roleMenus);
        }

        return result;
    }

}
