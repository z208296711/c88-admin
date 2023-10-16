package com.c88.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.SysOauthClientMapper;
import com.c88.admin.pojo.entity.SysOauthClient;
import com.c88.admin.service.ISysOauthClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient> implements ISysOauthClientService {

    private final StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    void init() {
        cleanCache();
    }

    /**
     * 清理客户端缓存
     */
    @Override
    public void cleanCache() {
        Set<String> keys = stringRedisTemplate.keys("auth:oauth-client:*");
        if (CollectionUtil.isNotEmpty(keys)) {
            stringRedisTemplate.delete(keys);
        }
    }

}
