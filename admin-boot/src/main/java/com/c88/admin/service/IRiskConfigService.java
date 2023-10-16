package com.c88.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.c88.admin.pojo.entity.RiskConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 風控規則
 */
@Service
public interface IRiskConfigService extends IService<RiskConfig> {

    RiskConfig one();

    boolean update(RiskConfig risk);

    RiskConfig getRiskConfigByParam(Map<String,Object> params);

}
