package com.c88.admin.pojo.entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SysRolePermission {
    private Long roleId;
    private Long permissionId;
}
