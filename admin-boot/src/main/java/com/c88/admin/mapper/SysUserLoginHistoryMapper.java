package com.c88.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.pojo.entity.SysUserLoginHistory;
import com.c88.admin.pojo.query.UserLoginPageQuery;
import com.c88.admin.pojo.vo.user.UserLoginHistoryPageVO;
import com.c88.admin.pojo.vo.user.UserLoginSummaryVO;
import com.c88.admin.pojo.vo.user.UserPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserLoginHistoryMapper extends BaseMapper<SysUserLoginHistory> {

    /**
     * 取得用户登入分页列表
     *
     * @param page
     * @param queryParams 查询参数
     * @return
     */
    List<UserLoginHistoryPageVO> listUsersLoginPage(Page<UserLoginHistoryPageVO> page, UserLoginPageQuery queryParams);

    /**
     * 取得用户登入詳細資訊
     */
    List<UserLoginSummaryVO> getLoginSummary(List<Long> userIdList);
}




