package com.c88.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.c88.admin.pojo.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<SysOperationLog> {

}