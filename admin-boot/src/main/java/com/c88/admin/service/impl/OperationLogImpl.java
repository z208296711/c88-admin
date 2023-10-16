package com.c88.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.OperationLogMapper;
import com.c88.admin.pojo.entity.SysMenu;
import com.c88.admin.pojo.entity.SysOperationLog;
import com.c88.admin.pojo.form.LogOpForm;
import com.c88.admin.service.IOperationLogService;
import com.c88.admin.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationLogImpl extends ServiceImpl<OperationLogMapper, SysOperationLog> implements IOperationLogService {

    private final ISysMenuService iSysMenuService;

    @Override
    public Page<SysOperationLog> getOperationLogList(LogOpForm form) {
        Page<SysOperationLog> page = new Page<>(form.getPageNum(), form.getPageSize());

        QueryWrapper<SysOperationLog> queryWrapper = new QueryWrapper<>();
        if (form.getDateFrom() != null) {
            queryWrapper.ge("operation_date", form.getDateFrom());
        }
        if (form.getDateTo() != null) {
            queryWrapper.le("operation_date", form.getDateTo());
        }

        Map<String, String> menuMap = iSysMenuService.getAllMenu().stream().collect(Collectors.toMap(m -> m.getId().toString(), SysMenu::getI18n));

        queryWrapper.eq(!StringUtils.isEmpty(form.getUserName()), "operator", form.getUserName());
        queryWrapper.eq(!StringUtils.isEmpty(form.getMenu()), "menu", menuMap.get(form.getMenu()));
        queryWrapper.eq(!StringUtils.isEmpty(form.getMenuPage()), "menu_page", menuMap.get(form.getMenuPage()));

        if (form.getLoginIp() != null && form.getLoginIp().length() > 0) {
            queryWrapper.eq("operator_login_ip", form.getLoginIp());
        }

        queryWrapper.orderByDesc("operation_date");

        return baseMapper.selectPage(page, queryWrapper);
    }
}
