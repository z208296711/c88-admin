package com.c88.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.SysUserLoginHistory;
import com.c88.admin.pojo.query.UserLoginPageQuery;
import com.c88.admin.pojo.vo.user.UserLoginHistoryPageVO;

/**
 *
 */
public interface ISysUserLoginHistoryService extends IService<SysUserLoginHistory> {
    /**
     * 用户分页列表
     *
     * @return
     */
    IPage<UserLoginHistoryPageVO> listUsersPage(UserLoginPageQuery queryParams);
}
