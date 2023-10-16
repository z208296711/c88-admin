package com.c88.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.c88.common.core.base.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Tag extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Size(max = 20)
    private String name;

}
