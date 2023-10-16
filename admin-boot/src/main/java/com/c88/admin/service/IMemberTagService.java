package com.c88.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.MemberTag;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 會員標籤對應
 */
@Service
public interface IMemberTagService extends IService< MemberTag> {

    boolean saveBatchIgnore(Collection<MemberTag> entityList);

}
