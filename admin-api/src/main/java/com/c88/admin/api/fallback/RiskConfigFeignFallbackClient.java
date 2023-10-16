package com.c88.admin.api.fallback;


import com.c88.admin.api.RiskConfigFeignClient;
import com.c88.admin.dto.RiskConfigDTO;
import com.c88.common.core.result.Result;
import com.c88.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RiskConfigFeignFallbackClient implements RiskConfigFeignClient {

    @Override
    public Result getRiskConfig() {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }

    @Override
    public Result<RiskConfigDTO> getRiskConfigByType(Integer type) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }

}
