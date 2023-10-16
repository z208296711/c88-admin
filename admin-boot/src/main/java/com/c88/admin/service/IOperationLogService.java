package com.c88.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.SysOperationLog;
import com.c88.admin.pojo.form.LogOpForm;

/**
 * @Created 2022/5/9
 */
public interface IOperationLogService extends IService<SysOperationLog> {

	Page<SysOperationLog> getOperationLogList(LogOpForm from);

}
