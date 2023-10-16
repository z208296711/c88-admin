package com.c88.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.SysMenu;
import com.c88.admin.pojo.vo.menu.DirectoryMenuVO;
import com.c88.admin.pojo.vo.menu.MenuTableVO;
import com.c88.admin.pojo.vo.menu.NextRouteVO;
import com.c88.member.vo.OptionVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> listPermRoles();

    /**
     * 刷新Redis缓存中角色菜单的权限规则，角色和菜单信息变更调用
     */
    boolean refreshPermRolesRules();

    /**
     * 根据角色编码集合获取按钮权限标识
     *
     * @param roles 角色权限编码集合
     * @return
     */
    List<String> listBtnPermByRoles(List<String> roles);

    /**
     * 菜单表格(Table)层级列表
     *
     * @param name 菜单名称
     * @return
     */
    List<MenuTableVO> findByRoleId(Long roleId);

    /**
     * 菜单表格(Table)层级列表
     *
     * @param name 菜单名称
     * @return
     */
    List<MenuTableVO> listTableMenus(String name);

    /**
     * 菜单下拉(Select)层级列表
     *
     * @return
     */
    List<OptionVO> listSelectMenus(boolean excludePermission);

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    boolean saveMenu(SysMenu menu);

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    boolean updateMenu(SysMenu menu);

    /**
     * 清理路由缓存
     */
    void cleanCache();

    /**
     * 获取路由列表(适配Vue3的vue-next-router)
     *
     * @return
     */
    List<NextRouteVO> listNextRoutes();

    void refreshPermRoles();

    List<DirectoryMenuVO> findDirectoryMenuOption();

    List<SysMenu> getAllMenu();

    Boolean uploadMenuExcel(MultipartFile file);
}
