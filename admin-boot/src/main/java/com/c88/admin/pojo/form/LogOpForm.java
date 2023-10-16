package com.c88.admin.pojo.form;

import com.c88.common.core.base.BasePageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LogOpForm extends BasePageQuery {

	@ApiModelProperty(value = "開始時間",required = false,example = "2020-01-01 00:00:00")
	private String dateFrom;

	@ApiModelProperty(value = "結束時間",required = false,example = "2020-12-01 00:00:00")
	private String dateTo;

	@ApiModelProperty(value = "管理者名稱", required = false)
	private String userName;

	@ApiModelProperty(value = "最後登入IP", required = false)
	private String loginIp;

	@ApiModelProperty(value = "選單", required =  false)
	private String menu;

	@ApiModelProperty(value = "選單頁面", required =  false)
	private String menuPage;

}
