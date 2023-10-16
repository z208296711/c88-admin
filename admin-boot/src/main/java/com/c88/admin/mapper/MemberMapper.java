package com.c88.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.pojo.entity.Member;
import com.c88.admin.pojo.query.MemberPageQuery;
import com.c88.admin.pojo.vo.member.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
    Page<MemberVO> queryMember(Page<MemberVO> page, @Param("queryParams") MemberPageQuery memberPageQuery);
}
