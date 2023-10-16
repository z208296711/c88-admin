package com.c88.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.SysRoleMapper;
import com.c88.admin.pojo.entity.SysRole;
import com.c88.admin.pojo.entity.SysRoleMenu;
import com.c88.admin.pojo.entity.SysUserRole;
import com.c88.admin.service.ISysRoleMenuService;
import com.c88.admin.service.ISysRoleService;
import com.c88.admin.service.ISysUserRoleService;
import com.c88.common.core.result.ResultCode;
import com.c88.common.web.exception.BizException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private ISysRoleMenuService iSysRoleMenuService;

    private ISysUserRoleService iSysUserRoleService;

    @Override
    public boolean delete(List<Long> ids) {
        Optional.ofNullable(ids).orElse(new ArrayList<>()).forEach(id -> {
            int count = iSysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
            if (count > 0) {
                throw new BizException(ResultCode.ROLE_DELETE_ERROR);
            }
            //  Assert.isTrue(count <= 0, "该角色已分配用户，无法删除");
            iSysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        });
        return this.removeByIds(ids);
    }

}
