package com.c88.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.SysRoleMenu;

import java.util.List;
import java.util.Set;

public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    List<Long> listMenuIds(Long roleId);

    /**
     * 修改角色菜单
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    boolean update(Long roleId, List<Long> menuIds);

    /**
     * 修改權限對應角色
     *
     * @param roleIds 角色id集合
     * @param menuId  權限id
     * @return
     */
    boolean update(Set<Long> roleIds, Long menuId);
}
