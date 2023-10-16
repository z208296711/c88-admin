package com.c88.admin.api.fallback;


import com.c88.admin.api.UserFeignClient;
import com.c88.admin.dto.UserLoginDto;
import com.c88.common.core.result.Result;
import com.c88.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author haoxr
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public Result getUserByUsername(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }

    @Override
    public Result<Boolean> addUserLoginRecord(UserLoginDto userForm) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.DEGRADATION);
    }
}
