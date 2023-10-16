package com.c88.admin.api.fallback;

import com.c88.admin.api.TagFeignClient;
import com.c88.admin.dto.TagDTO;
import com.c88.common.core.result.Result;
import com.c88.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TagFeignFallbackClient implements TagFeignClient {
    @Override
    public Result<List<TagDTO>> listTags() {
        log.info("降級");
        return Result.failed(ResultCode.DEGRADATION);
    }
}
