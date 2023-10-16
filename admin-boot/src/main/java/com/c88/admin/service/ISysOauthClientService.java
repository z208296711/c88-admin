package com.c88.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.SysOauthClient;


public interface ISysOauthClientService extends IService<SysOauthClient> {
    void cleanCache();
}
