package com.c88.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.dto.AuthUserDTO;
import com.c88.admin.mapper.SysUserLoginHistoryMapper;
import com.c88.admin.mapper.SysUserMapper;
import com.c88.admin.pojo.entity.SysUser;
import com.c88.admin.pojo.entity.SysUserRole;
import com.c88.admin.pojo.form.UserForm;
import com.c88.admin.pojo.query.UserPageQuery;
import com.c88.admin.pojo.vo.user.UserDetailVO;
import com.c88.admin.pojo.vo.user.UserLoginSummaryVO;
import com.c88.admin.pojo.vo.user.UserPageVO;
import com.c88.admin.service.ISysUserRoleService;
import com.c88.admin.service.ISysUserService;
import com.c88.feign.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final RedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ISysUserRoleService iSysUserRoleService;
    private final SysUserLoginHistoryMapper sysUserLoginHistoryMapper;
    private final AuthFeignClient authFeignClient;

    @Override
    public List<String> getUserByRoleId(Long roleId) {
        return this.baseMapper.getUserByRoleId(roleId);
    }

    @Override
    public List<String> findUserByRoleIds(List<Long> roleIds) {
        return this.baseMapper.findUserByRoleIds(roleIds);
    }

    /**
     * 获取用户分页列表
     *
     * @param queryParams
     * @return
     */
    @Override
    public IPage<UserPageVO> listUsersPage(UserPageQuery queryParams) {
        Page<UserPageVO> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        List<UserPageVO> list = this.baseMapper.listUsersPage(page, queryParams);
        List<Long> userIdList = list.stream().map(UserPageVO::getId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(userIdList)){
            return new Page<>(queryParams.getPageNum(),queryParams.getPageSize());
        }

        List<UserLoginSummaryVO> summary = this.sysUserLoginHistoryMapper.getLoginSummary(userIdList);
        Map<Long, UserLoginSummaryVO> summaryVOMap = summary.stream().collect(Collectors.toMap(UserLoginSummaryVO::getUserId, Function.identity()));
        list.forEach(vo -> {
            UserLoginSummaryVO summaryVO = summaryVOMap.getOrDefault(vo.getId(), null);
            if (summaryVO != null) {
                vo.setLastLoginIp(summaryVO.getLastLoginIp());
                vo.setLoginCount(summaryVO.getLoginCount());
            }
        });

        page.setRecords(list);
        return page;
    }

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return
     */
    @Override
    public boolean saveUser(UserForm userForm) {

        SysUser user = new SysUser();

        BeanUtil.copyProperties(userForm, user);
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));

        boolean result = this.save(user);
        if (result) {
            Long userId = user.getId();
            List<Long> roleIds = user.getRoleIds();
            List<SysUserRole> sysUserRoles = Optional.ofNullable(roleIds).orElse(new ArrayList<>())
                    .stream().map(roleId -> new SysUserRole(userId, roleId))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sysUserRoles)) {
                iSysUserRoleService.saveBatch(sysUserRoles);
            }
        }
        return result;
    }

    /**
     * 更新用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return
     */
    @Override
    @Transactional
    public boolean updateUser(Long userId, UserForm userForm) {
        SysUser user = this.getById(userId);
        Assert.isTrue(user != null, "用户不存在或已被删除");

        if (StringUtils.isNotBlank(userForm.getPassword())) {
            user.setPassword(passwordEncoder.encode(userForm.getPassword())); // 初始化默认密码
        }

        // 用户旧角色ID集合
        List<Long> oldRoleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        if (userForm.getRoleIds() != null) {
            // 用户新角色ID集合
            List<Long> newRoleIds = userForm.getRoleIds();


            // 新增的用户角色
            List<Long> addRoleIds = newRoleIds.stream().filter(roleId -> !oldRoleIds.contains(roleId)).collect(Collectors.toList());
            List<SysUserRole> addUserRoles = Optional.ofNullable(addRoleIds).orElse(new ArrayList<>())
                    .stream().map(roleId -> new SysUserRole(userId, roleId))
                    .collect(Collectors.toList());
            iSysUserRoleService.saveBatch(addUserRoles);

            // 删除的用户角色
            List<Long> removeRoleIds = oldRoleIds.stream().filter(roleId -> !newRoleIds.contains(roleId)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removeRoleIds)) {
                iSysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
                        .in(SysUserRole::getRoleId, removeRoleIds)
                );
            }

            BeanUtil.copyProperties(userForm, user);
            authFeignClient.cleanToken("c88-admin-web", user.getUsername());
        }

        return this.updateById(user);
    }

    @Override
    @Transactional
    public boolean removeById(String id) {
        SysUser sysUser = this.getById(id);
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserRole::getUserId, id);
        iSysUserRoleService.remove(queryWrapper);
        //移除正在登入的token
        authFeignClient.cleanToken("c88-admin-web", sysUser.getUsername());
        super.removeById(id);
        return true;
    }


    /**
     * 根据用户名获取认证信息
     *
     * @param username
     * @return
     */
    @Override
    public AuthUserDTO getAuthInfoByUsername(String username) {
        return this.baseMapper.getAuthInfoByUsername(username);
    }

    /**
     * 获取用户表单详情
     *
     * @param userId
     * @return
     */
    @Override
    public UserDetailVO getUserDetail(Long userId) {
        return this.baseMapper.getUserDetail(userId);
    }
}
    