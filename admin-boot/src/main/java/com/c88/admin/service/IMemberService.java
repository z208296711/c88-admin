package com.c88.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.Member;
import com.c88.admin.pojo.entity.MemberRemark;
import com.c88.admin.pojo.vo.member.MemberVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 會員管理
 */
@Service
public interface IMemberService extends IService<Member> {

   boolean addTags(Set<Long> ids, Integer tagId);

    boolean deleteTags(Set<Integer> ids);

    boolean addRemark(Long uid, String admin, String content);

    MemberVO getDetail(Long id);

    boolean updateMember(Long id, MemberVO memberVO);

 IPage<MemberRemark> findRemark(Long uid, IPage<MemberRemark> page);

}
