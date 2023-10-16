package com.c88.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.dto.AuthUserDTO;
import com.c88.admin.pojo.entity.SysUser;
import com.c88.admin.pojo.query.UserPageQuery;
import com.c88.admin.pojo.vo.user.UserDetailVO;
import com.c88.admin.pojo.vo.user.UserPageVO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     *
     * @param page
     * @param queryParams 查询参数
     * @return
     */
    List<UserPageVO> listUsersPage(Page<UserPageVO> page, UserPageQuery queryParams);

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return
     */
    UserDetailVO getUserDetail(Long userId);

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    AuthUserDTO getAuthInfoByUsername(String username);


    List<String> getUserByRoleId(Long roleId);

    List<String> findUserByRoleIds(List<Long> roleIds);
}
