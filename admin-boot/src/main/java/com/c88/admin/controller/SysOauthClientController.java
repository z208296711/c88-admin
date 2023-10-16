package com.c88.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.dto.AuthClientDTO;
import com.c88.admin.pojo.entity.SysOauthClient;
import com.c88.admin.service.ISysOauthClientService;
import com.c88.common.core.result.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "客户端接口")
@RestController
@RequestMapping("/api/v1/oauth-clients")
@Slf4j
@AllArgsConstructor
public class SysOauthClientController {

    private ISysOauthClientService iSysOauthClientService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "pageNum", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "clientId", value = "客户端ID", paramType = "query", dataType = "String")
    })
    @GetMapping
    public ResponseEntity<IPage<SysOauthClient>> list(long pageNum, long pageSize, String clientId) {
        IPage<SysOauthClient> sysOauthClientIPage = iSysOauthClientService.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysOauthClient>()
                        .like(StringUtils.isNotBlank(clientId), SysOauthClient::getClientId, clientId));
        return ResponseEntity.ok(sysOauthClientIPage);
    }

    @ApiOperation(value = "客户端详情")
    @ApiImplicitParam(name = "clientId", value = "客户端id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/{clientId}")
    public ResponseEntity<SysOauthClient> detail(@PathVariable String clientId) {
        SysOauthClient client = iSysOauthClientService.getById(clientId);
        return ResponseEntity.ok(client);
    }

    @ApiOperation(value = "新增客户端")
    @ApiImplicitParam(name = "client", value = "实体JSON对象", required = true, paramType = "body", dataType = "OauthClientDetails")
    @PostMapping
    public ResponseEntity<Boolean> add(@RequestBody SysOauthClient client) {
        boolean status = iSysOauthClientService.save(client);
        return ResponseEntity.ok(status);
    }

    @ApiOperation(value = "修改客户端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "客户端id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "client", value = "实体JSON对象", required = true, paramType = "body", dataType = "OauthClientDetails")
    })
    @PutMapping(value = "/{clientId}")
    public ResponseEntity<Boolean> update(
            @PathVariable String clientId,
            @RequestBody SysOauthClient client) {
        boolean status = iSysOauthClientService.updateById(client);
        return ResponseEntity.ok(status);
    }

    @ApiOperation(value = "删除客户端")
    @ApiImplicitParam(name = "ids", value = "id集合,以,拼接字符串", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public ResponseEntity<Boolean> delete(@PathVariable("ids") String ids) {
        boolean status = iSysOauthClientService.removeByIds(Arrays.asList(ids.split(",")));
        return ResponseEntity.ok(status);
    }

    @ApiOperation(hidden = true, value = "获取 OAuth2 客户端认证信息", notes = "Feign 调用")
    @GetMapping("/getOAuth2ClientById")
    public Result<AuthClientDTO> getOAuth2ClientById(@RequestParam String clientId) {
        SysOauthClient client = iSysOauthClientService.getById(clientId);
        Assert.isTrue(client != null, "OAuth2 客户端不存在");
        AuthClientDTO authClientDTO = new AuthClientDTO();
        BeanUtil.copyProperties(client, authClientDTO);
        return Result.success(authClientDTO);
    }
}
