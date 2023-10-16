package com.c88.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.common.constant.SystemConstants;
import com.c88.admin.common.enums.MenuTypeEnum;
import com.c88.admin.mapper.SysMenuMapper;
import com.c88.admin.pojo.entity.SysMenu;
import com.c88.admin.pojo.vo.menu.DirectoryMenuVO;
import com.c88.admin.pojo.vo.menu.MenuTableVO;
import com.c88.admin.pojo.vo.menu.NextRouteVO;
import com.c88.admin.pojo.vo.menu.RoleAndPermVO;
import com.c88.admin.service.ISysMenuService;
import com.c88.common.core.constant.GlobalConstants;
import com.c88.member.vo.OptionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        this.refreshPermRoles();
    }

    @Override
    public List<SysMenu> listPermRoles() {
        return this.baseMapper.listMenuPermRoles();
    }

    @Override
    public boolean refreshPermRolesRules() {
        redisTemplate.delete(Arrays.asList(GlobalConstants.URL_PERM_ROLES_KEY, GlobalConstants.BTN_PERM_ROLES_KEY));
        List<SysMenu> permissions = this.listPermRoles();
        if (CollectionUtil.isNotEmpty(permissions)) {
            // 初始化URL【权限->角色(集合)】规则
            List<SysMenu> urlPermList = permissions.stream()
                    .filter(item -> StrUtil.isNotBlank(item.getUrlPerm()))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(urlPermList)) {
                Map<String, List<String>> urlPermRoles = new HashMap<>();
                urlPermList.stream().forEach(item -> {
                    String perm = item.getUrlPerm();
                    List<String> roles = item.getRoles();
                    urlPermRoles.put(perm, roles);
                });
                redisTemplate.opsForHash().putAll(GlobalConstants.URL_PERM_ROLES_KEY, urlPermRoles);
                redisTemplate.convertAndSend("cleanRoleLocalCache", "true");
            }
            // 初始化URL【按钮->角色(集合)】规则
            List<SysMenu> btnPermList = permissions.stream()
                    .filter(item -> StrUtil.isNotBlank(item.getBtnPerm()))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(btnPermList)) {
                Map<String, List<String>> btnPermRoles = MapUtil.newHashMap();
                btnPermList.stream().forEach(item -> {
                    String perm = item.getBtnPerm();
                    List<String> roles = item.getRoles();
                    btnPermRoles.put(perm, roles);
                });
                redisTemplate.opsForHash().putAll(GlobalConstants.BTN_PERM_ROLES_KEY, btnPermRoles);
            }
        }
        return true;
    }

    @Override
    public List<String> listBtnPermByRoles(List<String> roles) {
        return this.baseMapper.listBtnPermByRoles(roles);
    }

    @Override
    public List<MenuTableVO> findByRoleId(Long roleId) {

        List<SysMenu> allMenuList = this.baseMapper.listRoutes(false);
        List<SysMenu> menuList = this.baseMapper.findByRoleId(roleId);

        allMenuList.forEach(sysMenu -> {
            if (menuList.stream()
                    .map(SysMenu::getId)
                    .anyMatch(id -> id.equals(sysMenu.getId()))) {
                sysMenu.setHave(true);
            }
        });

        return recursion(allMenuList);
    }

    /**
     * 菜单表格（Table）层级列表
     *
     * @param name 菜单名称
     * @return
     */
    @Override
    public List<MenuTableVO> listTableMenus(String name) {
        List<SysMenu> menuList = this.list(
                new LambdaQueryWrapper<SysMenu>()
                        .like(StrUtil.isNotBlank(name), SysMenu::getName, name)
                        .orderByAsc(SysMenu::getSort)
        );
        return recursion(menuList);
    }

    /**
     * 递归生成菜单表格层级列表
     *
     * @param menuList 菜单列表
     * @return 菜单列表
     */
    private static List<MenuTableVO> recursion(List<SysMenu> menuList) {
        List<MenuTableVO> menuTableList = new ArrayList<>();
        // 保存所有节点的 id
        Set<Long> nodeIdSet = menuList.stream()
                .map(SysMenu::getId)
                .collect(Collectors.toSet());
        for (SysMenu sysMenu : menuList) {
            // 不在节点 id 集合中存在的 id 即为顶级节点 id, 递归生成列表
            Long parentId = sysMenu.getParentId();
            if (!nodeIdSet.contains(parentId)) {
                menuTableList.addAll(recursionTableList(parentId, menuList));
                nodeIdSet.add(parentId);
            }
        }
        // 如果结果列表为空说明所有的节点都是独立分散的, 直接转换后返回
        if (menuTableList.isEmpty()) {
            return menuList.stream()
                    .map(item -> {
                        MenuTableVO menuTableVO = new MenuTableVO();
                        BeanUtil.copyProperties(item, menuTableVO);
                        return menuTableVO;
                    })
                    .collect(Collectors.toList());
        }
        return menuTableList;
    }

    /**
     * 递归生成菜单表格层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private static List<MenuTableVO> recursionTableList(Long parentId, List<SysMenu> menuList) {
        List<MenuTableVO> menuTableList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    MenuTableVO menuTableVO = new MenuTableVO();
                    BeanUtil.copyProperties(menu, menuTableVO);
                    List<MenuTableVO> children = recursionTableList(menu.getId(), menuList);

                    if (CollectionUtil.isNotEmpty(children)) {
                        menuTableVO.setChildren(children);
                    }
                    menuTableList.add(menuTableVO);
                });
        return menuTableList;
    }

    /**
     * 菜单下拉（Select）层级列表
     *
     * @return
     */
    @Override
    public List<OptionVO> listSelectMenus(boolean excludePermission) {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>()
                .ne(excludePermission, SysMenu::getType, MenuTypeEnum.PERMISSION.getValue())
                .orderByAsc(SysMenu::getSort));
        return recursionSelectList(SystemConstants.ROOT_MENU_ID, menuList);
    }

    /**
     * 递归生成菜单下拉层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private static List<OptionVO> recursionSelectList(Long parentId, List<SysMenu> menuList) {
        List<OptionVO> menuSelectList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    OptionVO optionVO = new OptionVO(menu.getId(), menu.getName());
                    List<OptionVO> children = recursionSelectList(menu.getId(), menuList);
                    if (CollectionUtil.isNotEmpty(children)) {
                        optionVO.setChildren(children);
                    }
                    menuSelectList.add(optionVO);
                });
        return menuSelectList;
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean saveMenu(SysMenu menu) {
        String path = menu.getPath();

        MenuTypeEnum menuType = menu.getType();  // 菜单类型
        switch (menuType) {
            case CATALOG: // 目录
                Assert.isTrue(path.startsWith("/"), "目录路由路径格式错误，必须以/开始");
                menu.setComponent("Layout");
                break;
            case EXTLINK: // 外链
                menu.setComponent(null);
                break;
        }

        boolean result = this.save(menu);
        if (result) {
            this.refreshPermRolesRules();
        }
        return result;
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean updateMenu(SysMenu menu) {
        String path = menu.getPath();

        MenuTypeEnum menuType = menu.getType();  // 菜单类型
        switch (menuType) {
            case CATALOG: // 目录
                Assert.isTrue(path.startsWith("/"), "目录路由路径格式错误，必须以/开始");
                menu.setComponent("Layout");
                break;
            case EXTLINK: // 外链
                menu.setComponent(null);
                break;
        }

        boolean result = this.updateById(menu);
        if (result) {
            this.refreshPermRolesRules();
        }
        return result;
    }

    /**
     * 清理路由缓存
     */
    @Override
    @CacheEvict(cacheNames = "system", key = "'routes'")
    public void cleanCache() {
    }

    /**
     * 获取路由列表(适配Vue3的vue-next-router)
     *
     * @return
     */
    @Override
    @Cacheable(cacheNames = "system", key = "'routes'")
    public List<NextRouteVO> listNextRoutes() {
        List<SysMenu> menuList = this.baseMapper.listRoutes(true);
        this.refreshPermRoles();
        return recursionNextRoute(SystemConstants.ROOT_MENU_ID, menuList);
    }

    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return
     */
    private List<NextRouteVO> recursionNextRoute(Long parentId, List<SysMenu> menuList) {
        List<NextRouteVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    NextRouteVO nextRouteVO = new NextRouteVO();

                    MenuTypeEnum menuTypeEnum = menu.getType();

                    if (MenuTypeEnum.MENU.equals(menuTypeEnum)) {
                        nextRouteVO.setName(menu.getPath()); //  根据name路由跳转 this.$router.push({path:xxx})
                    }
                    nextRouteVO.setPath(menu.getPath()); // 根据path路由跳转 this.$router.push({name:xxx})
                    nextRouteVO.setRedirect(menu.getRedirect());
                    nextRouteVO.setComponent(menu.getComponent());
                    nextRouteVO.setRedirect(menu.getRedirect());

                    NextRouteVO.Meta meta = new NextRouteVO.Meta();
                    meta.setTitle(menu.getName());
                    meta.setIcon(menu.getIcon());
                    meta.setRoles(menu.getRoles());
                    meta.setHidden(!GlobalConstants.STATUS_YES.equals(menu.getVisible()));
                    meta.setKeepAlive(true);

                    nextRouteVO.setMeta(meta);
                    List<NextRouteVO> children = recursionNextRoute(menu.getId(), menuList);
                    // 含有子节点的目录设置为可见
                    boolean alwaysShow = CollectionUtil.isNotEmpty(children) && children.stream().anyMatch(item -> item.getMeta().getHidden().equals(false));
                    meta.setAlwaysShow(alwaysShow);
                    nextRouteVO.setChildren(children);
                    list.add(nextRouteVO);
                }));
        return list;
    }

    @Override
    public void refreshPermRoles() {
        List<RoleAndPermVO> byRoleAndPerm = this.baseMapper.findByRoleAndPerm();
        Map<String, List<String>> menus = byRoleAndPerm.stream()
                .collect(Collectors.groupingBy(RoleAndPermVO::getName, Collectors.mapping(RoleAndPermVO::getPerm, Collectors.toList())));
        redisTemplate.delete(GlobalConstants.URL_PERM_ROLES_PERM_KEY);
        redisTemplate.opsForHash().putAll(GlobalConstants.URL_PERM_ROLES_PERM_KEY, menus);
    }

    @Override
    public List<DirectoryMenuVO> findDirectoryMenuOption() {
        List<SysMenu> sysMenus = this.lambdaQuery()
                .select(SysMenu::getId, SysMenu::getCatalogId, SysMenu::getI18n, SysMenu::getType)
                .in(SysMenu::getType, MenuTypeEnum.MENU, MenuTypeEnum.CATALOG, MenuTypeEnum.NULL)
                .list();

        return sysMenus.stream()
                .filter(filter -> Objects.equals(filter.getType(), MenuTypeEnum.CATALOG))
                .map(sysMenu -> {
                            List<OptionVO<Long>> option = sysMenus.stream()
                                    .filter(filter -> Objects.equals(filter.getCatalogId(), sysMenu.getId()) && filter.getType() == MenuTypeEnum.MENU)
                                    .map(sysMenuByMenu ->
                                            OptionVO.<Long>builder()
                                                    .value(sysMenuByMenu.getId())
                                                    .label(sysMenuByMenu.getI18n())
                                                    .build()
                                    )
                                    .collect(Collectors.toUnmodifiableList());

                            return DirectoryMenuVO.builder()
                                    .value(sysMenu.getId())
                                    .label(sysMenu.getI18n())
                                    .option(option)
                                    .build();
                        }
                )
                .collect(Collectors.toList());

    }

    public List<SysMenu> getAllMenu() {
        return this.baseMapper.selectList(new QueryWrapper<>());
    }

    @Transactional
    @Override
    public Boolean uploadMenuExcel(MultipartFile file) {
        try {
            ExcelReaderBuilder read = EasyExcelFactory.read(file.getInputStream());
            read.autoCloseStream(true);
            List<LinkedHashMap<Integer, String>> menusExcel = read.sheet(0).doReadSync();

            List<SysMenu> menus = menusExcel.stream()
                    .map(menu ->
                            SysMenu.builder()
                                    .id(Long.valueOf(menu.get(0)))
                                    .parentId(Long.valueOf(menu.get(1)))
                                    .catalogId(Long.valueOf(menu.get(2)))
                                    .name(menu.get(3))
                                    .path(menu.get(4))
                                    .component(menu.get(5))
                                    .icon(menu.get(6))
                                    .sort(Integer.valueOf(menu.get(7)))
                                    .visible(Integer.valueOf(menu.get(8)))
                                    .urlPerm(menu.get(9))
                                    .i18n(menu.get(10))
                                    .btnPerm(menu.get(11))
                                    .note(menu.get(12))
                                    .type(MenuTypeEnum.getEnum(Integer.valueOf(menu.get(13))))
                                    .build()
                    )
                    .collect(Collectors.toList());
            this.remove(Wrappers.lambdaQuery());
            this.saveBatch(menus);
            this.refreshPermRoles();
        } catch (IOException e) {
            log.info("uploadMenuExcel fail : {}", e);
            return false;
        }
        return true;
    }
}
