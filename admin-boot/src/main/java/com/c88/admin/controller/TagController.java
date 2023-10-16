package com.c88.admin.controller;

import com.c88.admin.dto.TagDTO;
import com.c88.admin.pojo.entity.Tag;
import com.c88.admin.service.ITagService;
import com.c88.common.core.base.BaseSetVO;
import com.c88.common.core.result.Result;
import com.c88.common.web.exception.BizException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 會員標籤管理
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "會員標籤")
@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
@Validated
public class TagController {

    private final ITagService iTagService;

    @Operation(summary = "會員標籤列表")
    @GetMapping("/list")
    public Result<List<TagDTO>> listTags() {
        return Result.success(iTagService.list().stream().map(t -> {
            TagDTO tagDTO = new TagDTO();
            BeanUtils.copyProperties(t, tagDTO);
            return tagDTO;
        }).collect(Collectors.toList()));
    }

    @Operation(summary = "新增會員標籤")
    @PostMapping("/{name}")
    public Result<Boolean> addTag(@PathVariable @Size(max = 20) String name) {
        // 標籤命名不可重複
        if (iTagService.lambdaQuery().eq(Tag::getName, name).count() > 0) {
            throw new BizException("label.toast02");
        }

        Tag tag = new Tag();
        tag.setName(name);
        return Result.judge(iTagService.save(tag));
    }

    @Operation(summary = "刪除會員標籤")
    @DeleteMapping("")
    public Result<Boolean> deleteTag(@RequestBody BaseSetVO<Integer> set) {
        return Result.judge(iTagService.delete(set.getIds()));
    }

    @Operation(summary = "修改會員標籤")
    @PutMapping("")
    public Result<Boolean> updateTag(@Valid @RequestBody Tag tag) {
        return Result.judge(iTagService.updateById(tag));
    }

}
