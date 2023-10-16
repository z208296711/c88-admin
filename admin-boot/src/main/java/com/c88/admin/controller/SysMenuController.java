package com.c88.admin.controller;

import com.c88.admin.pojo.vo.menu.DirectoryMenuVO;
import com.c88.admin.pojo.vo.menu.NextRouteVO;
import com.c88.admin.service.ISysMenuService;
import com.c88.common.core.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 菜单路由控制器
 */
@Tag(name = "菜单接口")
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Slf4j
public class SysMenuController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private static final List<String> UPLOAD_VALID_ENVIRONMENT = List.of("k8s_local", "k8s_dev", "local");

    private final ISysMenuService menuService;

    @ApiOperation(value = "菜单路由(Route)列表")
    @GetMapping("/route")
    public ResponseEntity<List<NextRouteVO>> getRouteList() {
        List<NextRouteVO> routeList = menuService.listNextRoutes();
        return ResponseEntity.ok(routeList);
    }

    @Operation(summary = "目錄菜單")
    @GetMapping("/directory/option")
    public Result<List<DirectoryMenuVO>> findDirectoryMenuOption() {
        return Result.success(menuService.findDirectoryMenuOption());
    }

    @PostMapping(value = "/upload/menu/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Boolean> uploadMenuExcel(@RequestParam MultipartFile file) {
        if (UPLOAD_VALID_ENVIRONMENT.contains(activeProfile)) {
            return Result.success(menuService.uploadMenuExcel(file));
        }

        return Result.success(Boolean.FALSE);
    }

}
