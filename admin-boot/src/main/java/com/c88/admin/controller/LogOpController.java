package com.c88.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.pojo.entity.SysOperationLog;
import com.c88.admin.pojo.form.LogOpForm;
import com.c88.admin.pojo.reponse.LogsDto;
import com.c88.admin.service.IOperationLogService;
import com.c88.admin.service.ISysMenuService;
import com.c88.common.core.result.PageResult;
import com.c88.common.core.result.Result;
import com.c88.member.vo.OptionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "操作日記接口")
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@Slf4j
public class LogOpController {

    private final IOperationLogService operationLogService;
    private final ISysMenuService iSysMenuService;

    @Operation(summary = "日記列表", description = "返回內容的content為i18n key 對應的文字內容中對應的參數陣列[{0},{1},{2},{3}]")
    @PostMapping("/list")
    public PageResult<LogsDto> getLogs(@RequestBody LogOpForm form) {
        Page<SysOperationLog> page = this.operationLogService.getOperationLogList(form);
        return PageResult.success(page.convert(log -> {
            LogsDto dto = new LogsDto();
            BeanUtils.copyProperties(log, dto);
            dto.setUserName(log.getOperator());
            dto.setLoginIp(log.getOperatorLoginIp());
            return dto;
        }));
    }

    @Operation(summary = "選單", description = "操作日誌選單")
    @GetMapping("/menulist")
    public Result<List<OptionVO>> getMenuMap() {
        List<OptionVO> optionVOS = iSysMenuService.listSelectMenus(true);
        return Result.success(optionVOS);
    }
}
