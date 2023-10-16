package com.c88.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c88.admin.dto.RiskConfigDTO;
import com.c88.admin.mapper.RiskConfigMapper;
import com.c88.admin.mapstruct.RiskConfigConverter;
import com.c88.admin.pojo.entity.RiskConfig;
import com.c88.admin.service.IRiskConfigService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.Map;
import java.util.Objects;

import static com.c88.common.core.constant.TopicConstants.RISK_CONFIG;

/**
 * 風控規則
 */
@Service
@AllArgsConstructor
public class RiskConfigServiceImpl extends ServiceImpl<RiskConfigMapper, RiskConfig> implements IRiskConfigService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static LambdaQueryWrapper<RiskConfig> ONE;

    private final RiskConfigConverter riskConfigConverter;

    @PostConstruct
    void init() {
        ONE = new LambdaQueryWrapper<RiskConfig>()
                .orderByDesc(RiskConfig::getGmtModified).last("limit 1");
    }

    public RiskConfig one() {
        RiskConfig risk = baseMapper.selectOne(ONE);
        if (risk == null) {
            risk = new RiskConfig();// 預設值
            risk.setWithdrawRechargeRatio(500);// %
            risk.setRewardBet(1000);
            risk.setHugeWithdraw(5000);
            risk.setEarnings(5000);
            save(risk);
        }
        return risk;
    }

    @Transactional
    public boolean update(RiskConfig risk) {
        if (risk.getId() == null) {
            RiskConfig one = baseMapper.selectOne(ONE);
            if (one != null) {
                risk.setId(one.getId());
            }
        }
        if (risk.getTagIds() == null) {
            risk.setTagIds(new Integer[]{});
        }
        boolean result = saveOrUpdate(risk);
        if (result) {
            kafkaTemplate.send(RISK_CONFIG, riskConfigConverter.toVO(risk));
        }
        return result;
    }

    @Override
    public RiskConfig getRiskConfigByParam(Map<String, Object> params) {
        QueryWrapper<RiskConfig> qw = new QueryWrapper<>();
        Integer type = (Integer) params.get("type");
        if (!Objects.isNull(type)) {
            qw.eq("type", type);
        }
        qw.last("limit 1");
        RiskConfig risk = baseMapper.selectOne(qw);
        if (risk == null) {
            risk = new RiskConfig();// 預設值
            if (type.equals(0)) {//member
                risk.setWithdrawRechargeRatio(500);// %
                risk.setRewardBet(1000);
                risk.setHugeWithdraw(5000);
                risk.setEarnings(5000);
                risk.setType(type);
            } else if (type.equals(1)) {//agent
                risk.setWithdrawRechargeRatio(0);
                risk.setRewardBet(0);
                risk.setHugeWithdraw(5000);
                risk.setEarnings(0);
                risk.setType(type);
            }
            save(risk);
        }
        return risk;
    }

}
