package com.c88.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.SysUserLoginHistoryMapper;
import com.c88.admin.pojo.entity.SysUserLoginHistory;
import com.c88.admin.pojo.query.UserLoginPageQuery;
import com.c88.admin.pojo.vo.user.UserLoginHistoryPageVO;
import com.c88.admin.service.ISysUserLoginHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SysUserLoginHistoryServiceImpl extends ServiceImpl<SysUserLoginHistoryMapper, SysUserLoginHistory>
        implements ISysUserLoginHistoryService {

    @Override
    public IPage<UserLoginHistoryPageVO> listUsersPage(UserLoginPageQuery queryParams) {
        Page<UserLoginHistoryPageVO> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        List<UserLoginHistoryPageVO> list = this.baseMapper.listUsersLoginPage(page, queryParams);
        page.setRecords(list);
        return page;
    }


}




