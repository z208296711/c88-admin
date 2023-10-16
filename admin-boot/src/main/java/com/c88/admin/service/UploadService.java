package com.c88.admin.service;

import com.c88.admin.common.enums.UploadTypeEnum;
import com.c88.storage.service.GCPFileUploadService;
import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final GCPFileUploadService gcpFileUploadService;

    @Value("${cloud-storage.gcp.cdn}")
    private String cdn;

    public String upload(Integer uploadType, String path, MultipartFile file) {
        UploadTypeEnum type = UploadTypeEnum.getEnum(uploadType);
        Blob blob = gcpFileUploadService.upload(file, path, Objects.equals(type, UploadTypeEnum.IMAGE));
        return cdn + blob.getName();
    }

}
