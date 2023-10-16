package com.c88.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c88.admin.pojo.entity.SysMenu;
import com.c88.admin.pojo.vo.menu.RoleAndPermVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    /**
     * 获取权限和拥有权限的角色映射
     *
     * @return
     */
    List<SysMenu> listMenuPermRoles();
    /**
     * 根据角色编码集合获取按钮权限
     *
     * @param roles
     * @return
     */
    List<String> listBtnPermByRoles(List<String> roles);

    List<SysMenu> listRoutes(boolean onlyMenu);

    List<SysMenu> findByRoleId(Long roleId);

    List<RoleAndPermVO> findByRoleAndPerm();
}
