package com.c88.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c88.admin.pojo.entity.Member;
import com.c88.admin.pojo.entity.MemberRemark;
import com.c88.admin.pojo.vo.member.MemberVO;
import com.c88.admin.service.IMemberService;
import com.c88.common.core.base.BaseSetVO;
import com.c88.common.core.base.BaseSingleVO;
import com.c88.common.core.enums.MemberStatusEnum;
import com.c88.common.core.result.PageResult;
import com.c88.common.core.result.Result;
import com.c88.common.web.util.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

import static com.c88.common.core.constant.RedisConstants.FREEZE_MEMBER;
import static com.c88.common.redis.utils.RedisUtils.buildKey;

/**
 * 會員管理
 */
@Tag(name = "會員管理")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService iMemberService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;

    /**
     * 會員token生成後失效分鐘，由sys_oauth_client.access_token_validity（秒）來
     */
    private Duration tokenExpire = Duration.ofMinutes(600);// 預設60分鐘

//    @Operation(summary = "會員列表")
//    @PostMapping("/query")
//    public PageResult<MemberVO> query(@RequestBody MemberPageQuery memberPageQuery) {
//        return PageResult.success(memberService.queryMember(memberPageQuery));
//    }

    @Operation(summary = "新增會員")
    @PostMapping
    public Result<Boolean> addMember(@RequestBody MemberVO m) {
        Member member = BeanUtil.copyProperties(m, Member.class);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return Result.judge(iMemberService.save(member));
    }

    @Operation(summary = "修改會員狀態")
    @PatchMapping(value = "/{id}/{status}")
    public Result updateMemberStatus(
            @Parameter(description = "會員ID") @PathVariable Long id,
            @Parameter(description = "會員狀態，1_啟用, 2_凍結") @PathVariable byte status) {
        LambdaUpdateWrapper<Member> updateWrapper = new LambdaUpdateWrapper<Member>()
                .eq(Member::getId, id);
        MemberStatusEnum memberStatus = MemberStatusEnum.getEnum(status);
        updateWrapper.set(Member::getStatus, memberStatus.getStatus());
        if (MemberStatusEnum.FREEZE == memberStatus) {// 凍結會員後要強制登出會員
            redisTemplate.opsForValue().set(buildKey(FREEZE_MEMBER, String.valueOf(id)), null, tokenExpire);
        } else {
            redisTemplate.delete(buildKey(FREEZE_MEMBER, String.valueOf(id)));
        }
        return Result.judge(iMemberService.update(updateWrapper));
    }

    @Operation(summary = "批量取消會員標籤")
    @DeleteMapping("/tag")
    public Result<Boolean> deleteTag(@RequestBody BaseSetVO<Integer> set) {
        return Result.judge(iMemberService.deleteTags(set.getIds()));
    }

    @Operation(summary = "批量設置會員標籤")
    @PutMapping("/tag/{tagId}")
    public Result<Boolean> updateTag(@RequestBody BaseSetVO<Long> set, @PathVariable("tagId") Integer tagId) {
        return Result.judge(iMemberService.addTags(set.getIds(), tagId));
    }

    @Operation(summary = "修改會員")
    @PutMapping(value = "/{id}")
    public Result updateMember(
            @Parameter(description = "會員ID") @PathVariable Long id,
            @Valid @RequestBody MemberVO memberVO) {
        return Result.judge(iMemberService.updateMember(id, memberVO));
    }

    @Operation(summary = "會員詳情")
    @GetMapping(value = "/{id}")
    public Result<MemberVO> id(@PathVariable Long id) {
        return Result.success(iMemberService.getDetail(id));
    }

    @Operation(summary = "修改會員密碼")
    @PatchMapping(value = "/{id}")
    public Result<Boolean> updateMemberPassword(
            @Parameter(description = "會員ID") @PathVariable Long id,
            @RequestBody MemberVO memberVO) {
        LambdaUpdateWrapper<Member> updateWrapper = new LambdaUpdateWrapper<Member>()
                .eq(Member::getId, id);
        updateWrapper.set(StringUtils.hasText(memberVO.getPassword()), Member::getPassword,
                passwordEncoder.encode(memberVO.getPassword()));
        updateWrapper.set(StringUtils.hasText(memberVO.getPassword()), Member::getDisplayPassword,
                String.valueOf(memberVO.getPassword().charAt(0)) + memberVO.getPassword().charAt(memberVO.getPassword().length() - 1));
        return Result.judge(iMemberService.update(updateWrapper));
    }

    @Operation(summary = "添加備註")
    @PostMapping("/remark/{id}")
    public Result<Boolean> addRemark(@Parameter(description = "會員ID") @PathVariable Long id,
                                     @RequestBody BaseSingleVO<String> baseSingleVO) {
        return Result.judge(iMemberService.addRemark(id, UserUtils.getUsername(), baseSingleVO.getValue()));
    }

    @Operation(summary = "查詢備註")
    @GetMapping("/remark/{id}")
    public PageResult<MemberRemark> findRemark(@Parameter(description = "會員ID") @PathVariable Long id, long pageNum, long pageSize) {
        return PageResult.success(iMemberService.findRemark(id,new Page<>(pageNum, pageSize)));
    }

}
