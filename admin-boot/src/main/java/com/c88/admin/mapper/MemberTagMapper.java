package com.c88.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c88.admin.pojo.entity.MemberTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberTagMapper extends BaseMapper<MemberTag> {

    @Insert("INSERT IGNORE INTO member_tag ( member_id, tag_id ) VALUES (#{memberId},#{tagId})")
    boolean insertIgnore(MemberTag memberTag);
}
