package com.c88.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.c88.admin.pojo.entity.SysUserLoginHistory;
import com.c88.admin.pojo.form.UserLoginForm;
import com.c88.admin.pojo.query.UserLoginPageQuery;
import com.c88.admin.pojo.vo.user.UserLoginHistoryPageVO;
import com.c88.admin.service.ISysUserLoginHistoryService;
import com.c88.common.core.result.PageResult;
import com.c88.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 */
@Tag(name = "用戶登入紀錄")
@RestController
@RequestMapping("/api/v1/users/loginRecord")
@RequiredArgsConstructor
public class SysUserLoginHistoryController {

    private final ISysUserLoginHistoryService iSysUserLoginHistoryService;


    @Operation(summary = "用户登入紀錄分页列表", description = "用户登入紀錄分页列表")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "返回結果", content = @Content(schema = @Schema(implementation = PageResult.class)))})
    @GetMapping("/page")
    public PageResult<UserLoginHistoryPageVO> listUsersPage(
           @ParameterObject UserLoginPageQuery queryParams
    ) {
        IPage<UserLoginHistoryPageVO> result = iSysUserLoginHistoryService.listUsersPage(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary = "新增用户登入紀錄",description = "新增用户登入紀錄")
    @PostMapping("")
    public Result<Boolean> addUser(@RequestBody UserLoginForm userForm) {
        SysUserLoginHistory sysUserLoginHistory = new SysUserLoginHistory();
        BeanUtil.copyProperties(userForm, sysUserLoginHistory);
        boolean result = iSysUserLoginHistoryService.save(sysUserLoginHistory);
        return Result.judge(result);
    }

}
