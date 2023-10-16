package com.c88.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.c88.common.core.base.BaseEntity;
import com.c88.common.web.log.OperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysOperationLog extends BaseEntity {

	@TableId(type = IdType.AUTO)
	private Long id;

	private Boolean success;
	private OperationEnum type;
	private String menu;
	private String menuPage;
	private LocalDateTime operationDate;
	private String operator;
	private String operatorLoginIp;
	private String content;
	private String beforeJson;
	private String afterJson;
	private String i18nKey;

//	public SysOperationLog(SysOperationLog operationLog) {
//		this.id = operationLog.id;
//		this.success = operationLog.success;
//		this.type = operationLog.type;
//		this.menu = operationLog.menu;
//		this.menuPage = operationLog.menuPage;
//		this.operationDate = operationLog.operationDate;
//		this.operator = operationLog.operator;
//		this.operatorLoginIp = operationLog.operatorLoginIp;
//		this.content = operationLog.content;
//		this.beforeJson = operationLog.beforeJson;
//		this.afterJson = operationLog.afterJson;
//	}
}
