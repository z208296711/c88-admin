package com.c88.admin.api;

import com.c88.admin.api.fallback.OAuthClientFeignFallbackClient;
import com.c88.admin.dto.AuthClientDTO;
import com.c88.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "c88-admin", path = "/admin", fallback = OAuthClientFeignFallbackClient.class)
public interface OAuthClientFeignClient {

    @GetMapping("/api/v1/oauth-clients/getOAuth2ClientById")
    Result<AuthClientDTO> getOAuth2ClientById(@RequestParam String clientId);
}
