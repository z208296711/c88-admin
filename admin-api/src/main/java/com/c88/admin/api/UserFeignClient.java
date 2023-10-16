package com.c88.admin.api;


import com.c88.admin.api.fallback.UserFeignFallbackClient;

import com.c88.admin.dto.AuthUserDTO;
import com.c88.admin.dto.UserLoginDto;
import com.c88.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "c88-admin", path = "/admin", fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

    @GetMapping("/api/v1/users/username/{username}")
    Result<AuthUserDTO> getUserByUsername(@PathVariable String username);

    @PostMapping("/api/v1/users/loginRecord")
    Result<Boolean> addUserLoginRecord(@RequestBody UserLoginDto userForm);

}
