package com.c88.admin.api.fallback;

import com.c88.admin.api.OAuthClientFeignClient;
import com.c88.admin.dto.AuthClientDTO;
import com.c88.common.core.result.Result;
import com.c88.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Slf4j
public class OAuthClientFeignFallbackClient implements OAuthClientFeignClient {

    @Override
    public Result<AuthClientDTO> getOAuth2ClientById(String clientId) {
        log.info("降級");
        return Result.failed(ResultCode.DEGRADATION);
    }

}
