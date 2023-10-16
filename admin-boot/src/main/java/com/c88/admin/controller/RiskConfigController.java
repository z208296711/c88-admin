package com.c88.admin.controller;

import com.c88.admin.dto.RiskConfigDTO;
import com.c88.admin.pojo.entity.RiskConfig;
import com.c88.admin.service.IRiskConfigService;
import com.c88.common.core.enums.RiskTypeEnum;
import com.c88.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "風控管理")
@RestController
@RequestMapping("/api/v1/riskConfig")
@Validated
@AllArgsConstructor
public class RiskConfigController {

    private final IRiskConfigService iRiskConfigService;

    @Operation(summary = "獲取規則")
    @GetMapping("/{type}")
    public Result<RiskConfig> getRisk(@PathVariable Integer type) {
        Map<String,Object> params = new HashMap<>();
        params.put("type", type);
        return Result.success(iRiskConfigService.getRiskConfigByParam(params));
    }

    @Operation(summary = "獲取規則")
    @GetMapping("/one/{type}")
    public Result<RiskConfigDTO> getRiskOneByType(@PathVariable Integer type) {
        Map<String,Object> params = new HashMap<>();
        params.put("type", type);
        RiskConfigDTO riskConfigDTO = new RiskConfigDTO();
        BeanUtils.copyProperties(iRiskConfigService.getRiskConfigByParam(params), riskConfigDTO);
        return Result.success(riskConfigDTO);
    }

    @Operation(summary = "獲取規則")
    @GetMapping("/one")
    public Result<RiskConfigDTO> getRiskOne() {
        RiskConfigDTO riskConfigDTO = new RiskConfigDTO();
        BeanUtils.copyProperties(iRiskConfigService.one(), riskConfigDTO);
        return Result.success(riskConfigDTO);
    }

    @Operation(summary = "設定規則")
    @PutMapping("")
    public Result<Boolean> updateRisk(@Valid @RequestBody RiskConfig risk) {
        return Result.success(iRiskConfigService.update(risk));
    }

    @Operation(summary = "風控維度，下拉選單")
    @GetMapping("/list")
    public Result<List<RiskTypeEnum>> list() {
        return Result.success(Arrays.asList(RiskTypeEnum.values()));
    }

}
