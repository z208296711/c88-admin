package com.c88.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TagDTO {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @Size(max = 20)
    private String name;

}
