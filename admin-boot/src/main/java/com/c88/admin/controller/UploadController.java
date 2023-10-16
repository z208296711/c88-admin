package com.c88.admin.controller;

import com.c88.admin.service.UploadService;
import com.c88.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "上傳")
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
@Slf4j
public class UploadController {

    private final UploadService uploadService;

    @Operation(summary = "上傳檔案")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload(@RequestParam(defaultValue = "0") @Parameter(description = "0圖片1不受限") Integer uploadType,
                                 @RequestParam @Parameter(description = "路徑") String path,
                                 @Parameter(description = "檔案") MultipartFile file) {
        return Result.success(uploadService.upload(uploadType, path, file));
    }
}
