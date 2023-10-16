package com.c88.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.dto.AuthUserDTO;
import com.c88.admin.pojo.entity.SysUser;
import com.c88.admin.pojo.form.UserForm;
import com.c88.admin.pojo.query.UserPageQuery;
import com.c88.admin.pojo.vo.user.UserDetailVO;
import com.c88.admin.pojo.vo.user.UserPageVO;

import java.util.List;


public interface ISysUserService extends IService<SysUser> {


    List<String> getUserByRoleId(Long roleId);

    List<String> findUserByRoleIds(List<Long> roleIds);

    /**
     * 用户分页列表
     *
     * @return
     */
    IPage<UserPageVO> listUsersPage(UserPageQuery queryParams);

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return
     */
    boolean saveUser(UserForm userForm);

    /**
     * 修改用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return
     */
    boolean updateUser(Long userId, UserForm userForm);


    boolean removeById(String id);

    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    AuthUserDTO getAuthInfoByUsername(String username);

    /**
     * 根据用户ID获取用户详情
     *
     * @param userId
     * @return
     */
    UserDetailVO getUserDetail(Long userId);
}
