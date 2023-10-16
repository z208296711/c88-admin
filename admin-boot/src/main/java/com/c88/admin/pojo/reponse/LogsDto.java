package com.c88.admin.pojo.reponse;

import com.alibaba.fastjson.annotation.JSONField;
import com.c88.common.web.log.OperationEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogsDto {

    private Boolean success;
    private OperationEnum type;
    private String menu;
    private String menuPage;
    private LocalDateTime operationDate;
    private String userName;
    private String loginIp;

    @Schema(title = "i18n key 對應的文字內容中對應的參數陣列[{0},{1},{2},{3}]")
    @JSONField(jsonDirect = true)
    private String content;
    private String beforeJson;
    private String afterJson;
    private String i18nKey;


}
