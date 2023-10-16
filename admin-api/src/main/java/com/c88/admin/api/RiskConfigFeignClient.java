package com.c88.admin.api;


import com.c88.admin.api.fallback.RiskConfigFeignFallbackClient;
import com.c88.admin.dto.RiskConfigDTO;
import com.c88.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "c88-admin", path = "/admin", fallback = RiskConfigFeignFallbackClient.class)
public interface RiskConfigFeignClient {

    @GetMapping("/api/v1/riskConfig/one")
    Result<RiskConfigDTO> getRiskConfig();

    @GetMapping("/api/v1/riskConfig/one/{type}")
    Result<RiskConfigDTO> getRiskConfigByType(@PathVariable("type") Integer type);


}
