package com.c88.admin.api;

import com.c88.admin.api.fallback.TagFeignFallbackClient;
import com.c88.admin.dto.TagDTO;
import com.c88.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "c88-admin", path = "/admin", fallback = TagFeignFallbackClient.class)
public interface TagFeignClient {

    @GetMapping("/api/v1/tag/list")
    Result<List<TagDTO>> listTags();
}
