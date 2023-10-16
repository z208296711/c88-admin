package com.c88.admin.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import com.c88.admin.pojo.entity.SysOperationLog;
import com.c88.admin.service.IOperationLogService;
import com.c88.common.web.log.IAnnoLogService;
import com.c88.common.web.log.LogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class AnnoLogServiceImpl implements IAnnoLogService<LogDTO> {

	private final IOperationLogService operationLogService;

	@Override
	public void saveAnnoLog(LogDTO logDTO) {

		SysOperationLog logVo = new SysOperationLog();
		BeanUtils.copyProperties(logDTO, logVo);

//		String ip = logDTO.getServletRequest() != null ? ServletUtil.getClientIP(logDTO.getServletRequest()) : "";
//		logVo.setOperatorLoginIp(ip);

		this.operationLogService.save(logVo);
	}

}
