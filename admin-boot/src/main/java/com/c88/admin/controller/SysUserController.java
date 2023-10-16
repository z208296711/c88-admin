package com.c88.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.c88.admin.dto.AuthUserDTO;
import com.c88.admin.pojo.entity.SysUser;
import com.c88.admin.pojo.form.UserForm;
import com.c88.admin.pojo.query.UserPageQuery;
import com.c88.admin.pojo.vo.user.LoginUserVO;
import com.c88.admin.pojo.vo.user.UserDetailVO;
import com.c88.admin.pojo.vo.user.UserPageVO;
import com.c88.admin.service.ISysMenuService;
import com.c88.admin.service.ISysUserService;
import com.c88.common.core.result.PageResult;
import com.c88.common.core.result.Result;
import com.c88.common.web.util.UserUtils;
import com.c88.feign.AuthFeignClient;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理
 */
@Tag(name = "後台用户管理")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService iSysUserService;

    private final ISysMenuService iSysMenuService;

    private final AuthFeignClient authFeignClient;

    @Operation(summary = "員工分页列表", description = "員工分页列表")
    @GetMapping("/page")
    public PageResult<UserPageVO> listUsersPage(UserPageQuery query) {
        IPage<UserPageVO> result = iSysUserService.listUsersPage(query);
        return PageResult.success(result);
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{userId}")
    public Result<UserDetailVO> getUserDetail(
            @Parameter(name = "用户ID") @PathVariable Long userId
    ) {
        UserDetailVO userDetail = iSysUserService.getUserDetail(userId);
        return Result.success(userDetail);
    }

    @Operation(summary = "新增用户", description = "新增用户")
    @PostMapping
    public Result<Boolean> addUser(@RequestBody UserForm userForm) {
        boolean result = iSysUserService.saveUser(userForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改用户", description = "修改用户")
    @PutMapping(value = "/{userId}")
    public Result<Boolean> updateUser(
            @ApiParam("用户ID") @PathVariable Long userId,
            @RequestBody UserForm userForm
    ) {
        boolean result = iSysUserService.updateUser(userId, userForm);
        
        return Result.judge(result);
    }

    @Operation(summary = "删除用户", description = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUsers(
            @ApiParam("用户ID，多个以英文逗号(,)分割") @PathVariable String id
    ) {

        boolean status = iSysUserService.removeById(id);

        return Result.judge(status);
    }
    
    @Operation(summary = "根据用户名获取认证信息", description = "提供用于用户登录认证信息")
    @GetMapping("/username/{username}")
    public Result<AuthUserDTO> getAuthInfoByUsername(
            @ApiParam("用户名") @PathVariable String username) {
        AuthUserDTO user = iSysUserService.getAuthInfoByUsername(username);
        return Result.success(user);
    }

    @Operation(summary = "获取当前登陆的用户信息")
    @GetMapping("/me")
    public Result<LoginUserVO> getCurrentUser() {
        LoginUserVO loginUserVO = new LoginUserVO();
        // 用户基本信息
        Long userId = UserUtils.getUserId();
        SysUser user = iSysUserService.getById(userId);
        BeanUtil.copyProperties(user, loginUserVO);
        // 用户角色信息
        List<String> roles = UserUtils.getRoles();
        loginUserVO.setRoles(roles);
        loginUserVO.setNickname(user.getUsername());

        // 用户按钮权限信息
        List<String> perms = iSysMenuService.listBtnPermByRoles(roles);
        loginUserVO.setPerms(perms);
        return Result.success(loginUserVO);
    }
}
