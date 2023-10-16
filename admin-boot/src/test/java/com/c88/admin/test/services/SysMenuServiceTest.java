package com.c88.admin.test.services;

import cn.hutool.core.lang.Assert;
import com.c88.admin.mapper.SysMenuMapper;
import com.c88.admin.service.impl.SysMenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;


@ExtendWith(MockitoExtension.class)
public class SysMenuServiceTest {

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private SysMenuMapper sysMenuMapper;

    @InjectMocks
    private SysMenuServiceImpl iSysMenuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenListPermRoles_thenReturnList() {
        Mockito.doReturn(Collections.emptyList()).when(sysMenuMapper).listMenuPermRoles();
        iSysMenuService.listPermRoles();
    }

    @Test
    void whenRefreshPermRolesRules_thenReturnTrue() {
        Assert.isTrue(iSysMenuService.refreshPermRolesRules());
    }

}
