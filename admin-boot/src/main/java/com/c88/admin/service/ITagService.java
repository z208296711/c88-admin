package com.c88.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 會員標籤
 */
@Service
public interface ITagService extends IService<Tag> {

    boolean delete(Set<Integer> ids);

}
