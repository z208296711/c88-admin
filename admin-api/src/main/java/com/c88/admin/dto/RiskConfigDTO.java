package com.c88.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.c88.common.core.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class RiskConfigDTO extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(title = "充提比")
    @Max(99999)
    @Min(0)
    private Integer withdrawRechargeRatio;
    @Schema(title = "投注中獎倍數")
    @Max(99999)
    @Min(0)
    private Integer rewardBet;
    @Schema(title = "大額提款")
    @Max(99999999)
    @Min(0)
    private Integer hugeWithdraw;
    @Schema(title = "當日盈利")
    @Max(99999999)
    @Min(0)
    private Integer earnings;

    @Schema(title = "關聯警告")
    private Boolean isRelate;
    @Schema(title = "資金異常")
    private Boolean isBalanceAbnormal;
    @Schema(title = "首提用戶")
    private Boolean isFirstWithdraw;
    @Schema(title = "修改信息後首提")
    private Boolean isFirstWithdrawAfterUpdated;
    @Schema(title = "無流水提款")
    private Boolean isNoTurnoverWithdraw;
    @Schema(title = "自動審單開關")
    private Boolean isAuto;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(title = "會員標籤")
    private Integer[] tagIds;

}
