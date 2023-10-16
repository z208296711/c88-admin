package com.c88.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.mapper.MemberMapper;
import com.c88.admin.mapper.MemberRemarkMapper;
import com.c88.admin.pojo.entity.Member;
import com.c88.admin.pojo.entity.MemberRemark;
import com.c88.admin.pojo.entity.MemberTag;
import com.c88.admin.pojo.vo.member.MemberVO;
import com.c88.admin.service.IMemberService;
import com.c88.admin.service.IMemberTagService;
import com.c88.common.core.result.Result;
import com.c88.common.core.util.Validate;
import com.c88.common.web.exception.BizException;
import com.c88.member.api.MemberFeignClient;
import com.c88.member.dto.MemberInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 會員管理
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    private final MemberFeignClient memberFeignClient;
    private final IMemberTagService IMemberTagService;
    private final MemberRemarkMapper memberRemarkMapper;

    public boolean addTags(Set<Long> ids, Integer tagId) {
        Set<MemberTag> memberTags = ids.parallelStream()
                .map(id -> new MemberTag(id, tagId)).collect(Collectors.toSet());
        return IMemberTagService.saveBatchIgnore(memberTags);
    }

    public boolean deleteTags(Set<Integer> ids) {
        IMemberTagService.getBaseMapper().delete(new LambdaQueryWrapper<MemberTag>()
                .in(MemberTag::getMemberId, ids));
        return true;
    }

    public boolean addRemark(Long uid, String admin, String content) {
        MemberRemark remark = new MemberRemark();
        remark.setAdmin(admin);
        remark.setUid(uid);
        remark.setContent(content);
        return memberRemarkMapper.insert(remark) > 0;
    }

    public MemberVO getDetail(Long id) {
        Member member = getById(id);

        MemberVO memberVO = BeanUtil.copyProperties(member, MemberVO.class);
        if (StringUtils.hasText(memberVO.getDisplayPassword())) {
            memberVO.setDisplayPassword(memberVO.getDisplayPassword().charAt(0) +
                    "****" + memberVO.getDisplayPassword().charAt(memberVO.getDisplayPassword().length() - 1));
        }
        MemberRemark remark = memberRemarkMapper.selectOne(new LambdaQueryWrapper<MemberRemark>()
                .eq(MemberRemark::getUid, id)
                .orderByDesc(MemberRemark::getGmtCreate)
                .last("limit 1"));
        memberVO.setLastRemark(remark != null ? remark.getContent() : null);

        // 處理vip轉換
        Map<Integer, String> vipConfigMap = new HashMap<>();
        Result<Map<Integer, String>> memberVipConfigResult = memberFeignClient.findMemberVipConfigMap();
        if (Result.isSuccess(memberVipConfigResult)) {
            vipConfigMap = memberVipConfigResult.getData();
        }

        // 取得會員vip
        Result<MemberInfoDTO> memberInfo = memberFeignClient.getMemberInfo(id);
        if (Result.isSuccess(memberInfo)) {
            memberVO.setVipLevel(vipConfigMap.getOrDefault(memberInfo.getData().getCurrentVipId(), ""));
        }

        return memberVO;
    }

    public boolean updateMember(Long id, MemberVO memberVO) {
        boolean hasMobile = StringUtils.hasText(memberVO.getMobile());
        boolean hasEmail = StringUtils.hasText(memberVO.getEmail());
        if (hasMobile && !Validate.phone(memberVO.getMobile())) {
            throw new BizException("login.phoneValidate");
        }
        if (hasEmail && !Validate.email(memberVO.getEmail())) {
            throw new BizException("login.emailValidate");
        }
        LambdaUpdateChainWrapper<Member> updateWrapper = lambdaUpdate()
                .eq(Member::getId, id)
                .set(StringUtils.hasText(memberVO.getAgent()), Member::getAgent, memberVO.getAgent())
                .set(StringUtils.hasText(memberVO.getRecommender()), Member::getRecommender, memberVO.getRecommender())
                .set(Member::getBirthday, memberVO.getBirthday())
                .set(Member::getRealName, memberVO.getRealName())
                .set(Member::getMobile, hasMobile ? memberVO.getMobile() : null)
                .set(Member::getEmail, hasEmail ? memberVO.getEmail() : null);

        return !StringUtils.hasText(updateWrapper.getWrapper().getSqlSet()) ||
                baseMapper.update(null, updateWrapper.getWrapper()) > 0;
    }


    public IPage<MemberRemark> findRemark(Long uid, IPage<MemberRemark> page) {
        return memberRemarkMapper.selectPage(page, new LambdaQueryWrapper<MemberRemark>()
                .eq(MemberRemark::getUid, uid).orderByDesc(MemberRemark::getGmtCreate)
        );
    }
}
