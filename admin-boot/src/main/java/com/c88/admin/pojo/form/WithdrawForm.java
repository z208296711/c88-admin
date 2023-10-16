package com.c88.admin.pojo.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Schema(title = "提款操作")
public class WithdrawForm {
    @Schema(title = "提款申請ID")
    Long id;
    @Size(max = 200)
    @Schema(title = "備註")
    String note;
    @Schema(title = "審級", description = "1_一審, 2_二審")
    int level;
    @Schema(title = "轉單對象帳號")
    String targetUsername;
}
