package com.c88.admin.test;

import com.c88.admin.mapper.SysMenuMapper;
import com.c88.admin.pojo.vo.menu.RoleAndPermVO;
import com.c88.common.core.constant.GlobalConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(properties = "spring.profiles.active:local")
public class RoleMenuTest {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void initPermMenu() {
        List<RoleAndPermVO> byRoleAndPerm = sysMenuMapper.findByRoleAndPerm();
        Map<String, List<String>> menus = byRoleAndPerm.stream()
                .collect(Collectors.groupingBy(RoleAndPermVO::getName, Collectors.mapping(RoleAndPermVO::getPerm, Collectors.toList())));
        redisTemplate.opsForHash().putAll(GlobalConstants.URL_PERM_ROLES_PERM_KEY, menus);
    }
}
