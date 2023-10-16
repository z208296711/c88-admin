package com.c88.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.MemberTagMapper;
import com.c88.admin.mapper.TagMapper;
import com.c88.admin.pojo.entity.MemberTag;
import com.c88.admin.pojo.entity.Tag;
import com.c88.admin.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 會員標籤
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    MemberTagMapper memberTagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Set<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        memberTagMapper.delete(new LambdaQueryWrapper<MemberTag>()
                .in(MemberTag::getTagId, ids));
        return true;
    }

}
