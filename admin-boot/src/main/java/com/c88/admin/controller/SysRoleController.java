package com.c88.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.pojo.entity.SysMenu;
import com.c88.admin.pojo.entity.SysRole;
import com.c88.admin.pojo.form.RoleMenuForm;
import com.c88.admin.pojo.vo.menu.MenuTableVO;
import com.c88.admin.pojo.vo.permission.RoleVO;
import com.c88.admin.service.ISysMenuService;
import com.c88.admin.service.ISysRoleMenuService;
import com.c88.admin.service.ISysRoleService;
import com.c88.admin.service.ISysUserService;
import com.c88.common.core.base.BaseSetVO;
import com.c88.common.core.constant.GlobalConstants;
import com.c88.common.core.result.PageResult;
import com.c88.common.core.result.Result;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Tag(name = "角色接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysMenuService iSysMenuService;
    private final ISysRoleService iSysRoleService;
    private final ISysRoleMenuService iSysRoleMenuService;
    private final RedisTemplate redisTemplate;
    private final ISysUserService iSysUserService;

    private static final String FIRST_CAN_BE_TRANSFER_PERMISSIONS = "risk:risk-first-review:beTransfer";
    private static final String SECOND_CAN_BE_TRANSFER_PERMISSIONS = "risk:risk-second-review:beTransfer";
    private static final String FIRST_CAN_BE_TRANSFER_PERMISSIONS_AGNET = "agentRisk:risk-first-review:beTransfer";
    private static final String SECOND_CAN_BE_TRANSFER_PERMISSIONS_AGENT = "agentRisk:risk-second-review:beTransfer";
    @Operation(summary = "列表分页")
    @GetMapping("/page")
    public PageResult<RoleVO> getRolePageList(
            @RequestParam(required = false, defaultValue = "1") long pageNum,
            @RequestParam(required = false, defaultValue = "20") long pageSize
    ) {
        Page<SysRole> result = iSysRoleService.page(new Page<>(pageNum, pageSize));
        IPage<RoleVO> resultVO = result.convert(RoleVO::new);
        return PageResult.success(resultVO);
    }

    @Operation(summary = "角色列表")
    @GetMapping
    public Result<List<RoleVO>> getRoleList() {

        List<SysRole> list = iSysRoleService.list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, GlobalConstants.STATUS_YES)
                .orderByAsc(SysRole::getSort)
        );
        List<RoleVO> roleVOList = list.stream().map(RoleVO::new).collect(Collectors.toList());
        return Result.success(roleVOList);
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public Result<Boolean> saveRole(@RequestBody RoleVO roleVO) {
        int count = iSysRoleService.count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, roleVO.getName())
                .or()
                .eq(SysRole::getName, roleVO.getName())
        );
        Assert.isTrue(count == 0, "角色名称或角色编码重复，请检查！");

        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(roleVO, sysRole);
        sysRole.setCode(roleVO.getName());
        boolean result = iSysRoleService.save(sysRole);
        if (result) {
            iSysMenuService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }

    @Operation(summary = "修改角色")
    @PutMapping(value = "/{id}")
    public Result<Boolean> updateRole(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @RequestBody RoleVO roleVO) {
        int count = iSysRoleService.count(new LambdaQueryWrapper<SysRole>()
                .ne(SysRole::getId, id)
                .and(wrapper ->
                        wrapper.eq(SysRole::getName, roleVO.getName())
                ));
        Assert.isTrue(count == 0, "角色名称或角色编码重复，请检查！");
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(roleVO, sysRole);
        sysRole.setCode(roleVO.getName());
        boolean result = iSysRoleService.updateById(sysRole);
        if (result) {
            iSysMenuService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{ids}")
    public Result<Boolean> deleteRoles(
            @ApiParam("删除角色，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = iSysRoleService.delete(Arrays.asList(ids.split(",")).stream()
                .map(Long::parseLong).collect(Collectors.toList()));
        if (result) {
            iSysMenuService.refreshPermRolesRules();
        }
        return Result.judge(result);
    }


    @Operation(summary = "获取角色的菜单ID集合")
    @GetMapping("/{roleId}/menu")
    public Result<List<MenuTableVO>> listRoleMenuIds(
            @PathVariable(value = "roleId") Long roleId
    ) {

        List<MenuTableVO> list = iSysMenuService.findByRoleId(roleId);
        return Result.success(list);
    }


    @Operation(summary = "修改角色菜单")
    @CacheEvict(cacheNames = "system", key = "'routes'")
    @PutMapping(value = "/{roleId}/menus")
    public Result<List<Long>> updateRoleMenu(
            @ApiParam("角色ID") @PathVariable Long roleId,
            @RequestBody RoleMenuForm menuForm
    ) {
        List<Long> menuIds = menuForm.getMenuIds();
        boolean result = iSysRoleMenuService.update(roleId, menuIds);
        if (result) {
            iSysMenuService.refreshPermRolesRules();
            iSysMenuService.refreshPermRoles();
        }
        return Result.judge(result);
    }

    @Operation(summary = "可被轉單角色列表")
    @GetMapping("/transfer/roles/{level}/{type}")
    public Result<List<RoleVO>> transferRoles(@Parameter(description = "審級，1_一審, 2_二審", example = "1") @PathVariable Integer level,@PathVariable Integer type) {
        List<SysRole> list = iSysRoleService.list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, GlobalConstants.STATUS_YES)
                .orderByAsc(SysRole::getSort)
        );
        List<RoleVO> roleVOList = list.stream().map(RoleVO::new).collect(Collectors.toList());
        Object object = null;
        if (type.equals(1)) {//代理
            object = redisTemplate.opsForHash().get(GlobalConstants.BTN_PERM_ROLES_KEY, level == 1 ? FIRST_CAN_BE_TRANSFER_PERMISSIONS_AGNET : SECOND_CAN_BE_TRANSFER_PERMISSIONS_AGENT);
        }else {//會員
            object = redisTemplate.opsForHash().get(GlobalConstants.BTN_PERM_ROLES_KEY, level == 1 ? FIRST_CAN_BE_TRANSFER_PERMISSIONS : SECOND_CAN_BE_TRANSFER_PERMISSIONS);
        }
        if (object != null) {
            List<String> hasPermissionRoles = Convert.toList(String.class, object);
            roleVOList.parallelStream().forEach(r -> r.setHave(hasPermissionRoles.contains(r.getCode())));
        }
        return Result.success(roleVOList);
    }

    @Operation(summary = "修改可被轉單角色列表")
    @PutMapping("/transfer/roles/{level}/{type}")
    public Result<Boolean> updateTransferRoles(@Parameter(description = "審級，1_一審, 2_二審", example = "1") @PathVariable Integer level,
                                               @Schema(title = "角色id集合") @RequestBody BaseSetVO<Long> ids,@PathVariable Integer type) {
        Long menuId;
        if (type.equals(1)) {//代理
            menuId = iSysMenuService.getOne(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getBtnPerm, level == 1 ? FIRST_CAN_BE_TRANSFER_PERMISSIONS_AGNET : SECOND_CAN_BE_TRANSFER_PERMISSIONS_AGENT)).getId();
        } else {//會員
            menuId = iSysMenuService.getOne(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getBtnPerm, level == 1 ? FIRST_CAN_BE_TRANSFER_PERMISSIONS : SECOND_CAN_BE_TRANSFER_PERMISSIONS)).getId();
        }
        boolean result = iSysRoleMenuService.update(ids.getIds(), menuId);
        if (result) {
            iSysMenuService.refreshPermRolesRules();
        }
        return Result.success(result);
    }

    @Operation(summary = "可被轉單人員清單")
    @GetMapping("/transfer/list/{level}/{type}")
    public Result<List<String>> transferList(@PathVariable Integer level,@PathVariable Integer type) {
        Object object;
        if(type.equals(1)){
            object= redisTemplate.opsForHash().get(GlobalConstants.BTN_PERM_ROLES_KEY, level == 1 ? FIRST_CAN_BE_TRANSFER_PERMISSIONS_AGNET : SECOND_CAN_BE_TRANSFER_PERMISSIONS_AGENT);
        }else {
            object = redisTemplate.opsForHash().get(GlobalConstants.BTN_PERM_ROLES_KEY, level == 1 ? FIRST_CAN_BE_TRANSFER_PERMISSIONS : SECOND_CAN_BE_TRANSFER_PERMISSIONS);
        }

        List<String> hasPermissionRoles = object != null ? Convert.toList(String.class, object) : null;
        List<SysRole> roles = iSysRoleService.list(new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getCode, hasPermissionRoles));

        return Result.success(isNotEmpty(roles) ?
                iSysUserService.findUserByRoleIds(roles.parallelStream().map(SysRole::getId).collect(Collectors.toList())) : new ArrayList<>());
    }

}
